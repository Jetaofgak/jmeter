package com.example.tpEvaluation.controller;



import com.example.tpEvaluation.model.Item;
import com.example.tpEvaluation.repository.CategoryRepository;
import com.example.tpEvaluation.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/items")
public class ItemController {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    // GET /items - Liste paginée
    @GetMapping
    public ResponseEntity<Page<Item>> getAllItems(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Item> items = itemRepository.findAll(pageable);
        return ResponseEntity.ok(items);
    }

    // GET /items/{id} - Par ID
    @GetMapping("/{id}")
    public ResponseEntity<Item> getItemById(@PathVariable Long id) {
        return itemRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET /items/sku/{sku} - Par SKU
    @GetMapping("/sku/{sku}")
    public ResponseEntity<Item> getItemBySku(@PathVariable String sku) {
        return itemRepository.findBySku(sku)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET /items/category/{categoryId} - Par catégorie
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Page<Item>> getItemsByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Item> items = itemRepository.findByCategoryId(categoryId, pageable);
        return ResponseEntity.ok(items);
    }

    // POST /items - Créer
    @PostMapping
    public ResponseEntity<Item> createItem(@RequestBody Item item) {
        // Vérifier que la catégorie existe
        if (item.getCategory() == null || item.getCategory().getId() == null) {
            return ResponseEntity.badRequest().build();
        }

        return categoryRepository.findById(item.getCategory().getId())
                .map(category -> {
                    item.setCategory(category);
                    Item savedItem = itemRepository.save(item);
                    return ResponseEntity.status(HttpStatus.CREATED).body(savedItem);
                })
                .orElse(ResponseEntity.badRequest().build());
    }

    // PUT /items/{id} - Modifier
    @PutMapping("/{id}")
    public ResponseEntity<Item> updateItem(
            @PathVariable Long id,
            @RequestBody Item itemDetails) {
        return itemRepository.findById(id)
                .map(item -> {
                    item.setSku(itemDetails.getSku());
                    item.setName(itemDetails.getName());
                    item.setPrice(itemDetails.getPrice());
                    item.setStock(itemDetails.getStock());

                    // Mise à jour de la catégorie si fournie
                    if (itemDetails.getCategory() != null &&
                            itemDetails.getCategory().getId() != null) {
                        categoryRepository.findById(itemDetails.getCategory().getId())
                                .ifPresent(item::setCategory);
                    }

                    Item updated = itemRepository.save(item);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /items/{id} - Supprimer
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        if (itemRepository.existsById(id)) {
            itemRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
