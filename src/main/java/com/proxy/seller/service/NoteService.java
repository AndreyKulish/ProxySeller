package com.proxy.seller.service;

import com.proxy.seller.data.Note;
import com.proxy.seller.data.User;
import com.proxy.seller.repository.NoteRepository;
import com.proxy.seller.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Сервисный класс, который обрабатывает запросы, связанные с заметками.
 */
@Service
public class NoteService {

    private final NoteRepository noteRepository;
    private final UserRepository userRepository;

    /**
     * Конструктор класса NoteService.
     *
     * @param noteRepository Репозиторий заметок
     * @param userRepository Репозиторий пользователей
     */
    @Autowired
    public NoteService(NoteRepository noteRepository, UserRepository userRepository) {
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
    }

    /**
     * Получает список заметок, отсортированных по дате создания.
     *
     * @return Список заметок
     */
    public List<Note> getAllNotesSortedByDate() {
        Sort sortByDate = Sort.by("createdAt").descending();
        return noteRepository.findAll(sortByDate);
    }

    /**
     * Получает список всех заметок.
     *
     * @return Список заметок
     */
    public List<Note> getAll() {
        return noteRepository.findAll();
    }

    /**
     * Получает заметку по заданному идентификатору.
     *
     * @param id Идентификатор заметки
     * @return Заметка с заданным идентификатором, либо код ошибки 404, если заметка не найдена
     */
    public ResponseEntity<Note> findById(String id) {
        Note note = noteRepository.findById(id).orElse(null);
        if (note == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(note, HttpStatus.OK);
    }

    /**
     * Создает новую заметку с заданным текстом.
     *
     * @param text Текст заметки
     * @return Созданную заметку
     */
    public ResponseEntity<Note> createNote(String text) {
        Note note = new Note(text);
        noteRepository.save(note);
        return new ResponseEntity<>(note, HttpStatus.CREATED);
    }

    /**
     * Обновляет текст заметки с заданным идентификатором.
     *
     * @param id   Идентификатор заметки
     * @param text Новый текст заметки
     * @return Обновленную заметку, либо код ошибки 404, если заметка не найдена, или 200, если заметка была успешно обновлена
     */
    public ResponseEntity<Note> updateNote(String id, String text) {
        Note note = noteRepository.findById(id).orElse(null);
        if (note == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        note.setText(text);
        noteRepository.save(note);
        return new ResponseEntity<>(note, HttpStatus.OK);
    }

    /**
     * Удаляет заметку с заданным идентификатором.
     *
     * @param id Идентификатор заметки
     * @return Код успешного выполнения, либо код ошибки 404, если заметка не найдена
     */
    public ResponseEntity<HttpStatus> deleteNote(String id) {
        Note note = noteRepository.findById(id).orElse(null);
        if (note == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        noteRepository.delete(note);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Добавляет лайк заметке с заданным идентификатором от зарегистрированного пользователя.
     *
     * @param id   Идентификатор заметки
     * @param user Пользователь, который ставит лайк
     * @return Код успешного выполнения, либо код ошибки 401, если пользователь не авторизован,
     * или код ошибки 404, если заметка не найдена
     */
    public ResponseEntity<HttpStatus> addLike(String id, User user) {
        Optional<User> userOptional = userRepository.findByUsername(user.getUsername());
        if (!userOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<Note> noteOptional = noteRepository.findById(id);
        if (!noteOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Note note = noteOptional.get();
        note.incrementLikes();
        noteRepository.save(note);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Убирает лайк с заметки с заданным идентификатором от зарегистрированного пользователя.
     *
     * @param id   Идентификатор заметки
     * @param user Пользователь, который убирает лайк
     * @return Код успешного выполнения, либо код ошибки 401, если пользователь не авторизован,
     * или код ошибки 404, если заметка не найдена
     */
    public ResponseEntity<HttpStatus> removeLike(String id, User user) {
        Optional<User> userOptional = userRepository.findByUsername(user.getUsername());
        if (!userOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<Note> noteOptional = noteRepository.findById(id);
        if (!noteOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Note note = noteOptional.get();
        note.decrementLikes();
        noteRepository.save(note);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
