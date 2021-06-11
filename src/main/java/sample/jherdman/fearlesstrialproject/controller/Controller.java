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
    public DataItem[] getItems() {
        return currentItems.toArray(new DataItem[0]);
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

    @DeleteMapping("/items")
    public ResponseEntity<String> deleteItems() {
        currentItems.clear();

        return ResponseEntity.ok().build();
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
                .anyMatch(i -> Collections.frequency(currentIdSet, i) > 1);
    }
}
