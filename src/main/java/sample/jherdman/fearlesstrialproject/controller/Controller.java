package sample.jherdman.fearlesstrialproject.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sample.jherdman.fearlesstrialproject.model.DataItem;

import java.util.*;
import java.util.stream.Collectors;

@RestController
public class Controller {

    private final HashSet<DataItem> currentItems;

    public Controller() {
        currentItems = new HashSet<>();
    }

    @GetMapping(value = "/items")
    @ResponseBody
    public DataItem[] getItems() {
        return currentItems.toArray(new DataItem[0]);
    }

    @GetMapping(value = "/items/{id}")
    public ResponseEntity<DataItem> getItem(@PathVariable int id) {
        Optional<DataItem> result = findItemById(id);

        if(result.isPresent()) {
            return ResponseEntity.ok(result.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/items")
    public ResponseEntity<String> addItems(@RequestBody DataItem[] newItems) {
        if(hasDuplicateIds(newItems)) {
            return ResponseEntity.badRequest().body("Provided items cannot have duplicate 'id' fields");
        } else if(hasExistingIds(newItems)) {
            return ResponseEntity.badRequest().body("Provided items id already exists");
        } else {
            currentItems.addAll(Arrays.asList(newItems));
        }

        return ResponseEntity.ok().build();
    }

    @PutMapping("/items")
    public ResponseEntity<String> updateItem(@RequestBody DataItem newItem) {
        Optional<DataItem> result = findItemById(newItem.getId());

        if(result.isPresent()) {
            result.get().setName(newItem.getName());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<String> deleteItem(@PathVariable int id) {
        Optional<DataItem> result = findItemById(id);
        if(result.isPresent()) {
            currentItems.remove(result.get());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/items")
    public void deleteItems() {
        currentItems.clear();
    }

    //
    // Helper functions
    //

    /*
     * Lookup function to find an item by id.
     * Will throw runtime exception if there are
     */
    Optional<DataItem> findItemById(int id) {
        List<DataItem> results = currentItems.stream()
                .filter(item -> item.getId() == id)
                .collect(Collectors.toList());

        if(results.size() > 1) {
            throw new RuntimeException("Multiple data items with an 'id' of " + id);
        }

        return results.size() == 0 ? Optional.empty() : Optional.of(results.get(0));
    }

    /*
     * Checks that the provided array of items has any duplicate id fields
     */
    boolean hasDuplicateIds(DataItem[] items) {
        if(items.length <= 1)
            return false;

        List<Integer> itemIdCollection = Arrays.stream(items)
                .map(DataItem::getId)
                .collect(Collectors.toList());

        return itemIdCollection.stream()
                .anyMatch(i -> Collections.frequency(itemIdCollection, i) > 1);
    }

    /*
     * Checks that the provided array of items do not have an id that is already present in the stored items.
     */
    boolean hasExistingIds(DataItem[] items) {
        Set<Integer> currentIdSet = currentItems.stream()
                .map(DataItem::getId)
                .collect(Collectors.toSet());

        return Arrays.stream(items)
                .map(DataItem::getId)
                .anyMatch(currentIdSet::contains);
    }
}
