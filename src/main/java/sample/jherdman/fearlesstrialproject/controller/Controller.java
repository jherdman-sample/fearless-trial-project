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
        DataItem result = findItemById(id);
        if(result == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(result);
        }
    }

    @PostMapping("/items")
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
    public ResponseEntity<String> addItem(@RequestBody DataItem newItem) {
        if(!hasExistingIds(new DataItem[]{newItem})) {
            return ResponseEntity.badRequest().body("Provided item id does not exist.");
        } else {
            findItemById(newItem.getId()).setName(newItem.getName());
        }

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/items")
    @ResponseBody
    public void deleteItems() {
        currentItems.clear();
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<String> deleteItem(@PathVariable int id) {
        DataItem result = findItemById(id);
        if(result == null) {
            return ResponseEntity.notFound().build();
        } else {
            currentItems.remove(findItemById(id));
            return ResponseEntity.ok().build();
        }
    }

    DataItem findItemById(int id) {
        List<DataItem> results = currentItems.stream()
                .filter(item -> item.getId() == id)
                .collect(Collectors.toList());

        if(results.size() > 1) {
            throw new RuntimeException("Multiple data items with an 'id' of " + id);
        }

        return results.size() == 0 ? null : results.get(0);
    }

    boolean hasDuplicateIds(DataItem[] items) {
        List<Integer> itemIdCollection = Arrays.stream(items)
                .map(DataItem::getId)
                .collect(Collectors.toList());

        return itemIdCollection.stream()
                .anyMatch(i -> Collections.frequency(itemIdCollection, i) > 1);
    }

    boolean hasExistingIds(DataItem[] items) {
        Set<Integer> currentIdSet = currentItems.stream()
                .map(DataItem::getId)
                .collect(Collectors.toSet());

        return Arrays.stream(items)
                .map(DataItem::getId)
                .anyMatch(currentIdSet::contains);
    }
}
