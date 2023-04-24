package com.proxy.seller.presentation;

import com.proxy.seller.data.Note;
import com.proxy.seller.data.User;
import com.proxy.seller.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    private final NoteService noteService;

    @Autowired
    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping("/list")
    public List<Note> getAllNotes() {
        return noteService.getAll();
    }

    @GetMapping("/list/sorted")
    public List<Note> getAllNotesSorted() {
        return noteService.getAllNotesSortedByDate();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Note> getNoteById(@PathVariable String id) {
        return noteService.findById(id);
    }

    @PostMapping("/create")
    public ResponseEntity<Note> createNote(@RequestBody String text) {
        return noteService.createNote(text);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Note> updateNoteById(@PathVariable String id, @RequestBody String text) {
        return noteService.updateNote(id, text);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteNoteById(@PathVariable String id) {
        return noteService.deleteNote(id);
    }

    @PutMapping("/{id}/like")
    public ResponseEntity<HttpStatus> addLike(@PathVariable String id, User user) {
        return noteService.addLike(id, user);
    }

    @PutMapping("/{id}/unlike")
    public ResponseEntity<HttpStatus> removeLike(@PathVariable String id, User user) {
        return noteService.removeLike(id, user);
    }
}
