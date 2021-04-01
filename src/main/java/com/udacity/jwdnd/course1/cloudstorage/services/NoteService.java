package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.DTO.NoteDTO;
import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    private final NoteMapper noteMapper;
    private final UserService userService;

    public NoteService(NoteMapper noteMapper, UserService userService) {
        this.noteMapper = noteMapper;
        this.userService = userService;
    }

    public int insertNote(NoteDTO noteDTO, Authentication auth) {
        User user = userService.getUserByUsername(auth.getName());
        return noteMapper.insertNote(new Note(null, noteDTO.getNoteTitle(), noteDTO.getNoteDescription(), user.getUserId()));
    }

    public List<Note> getAllNotes(int userId) {
        return noteMapper.getAllNotes(userId);
    }

    public Note getNotById(int noteId) {
        return noteMapper.getNoteById(noteId);
    }

    public int updateNote(NoteDTO noteDTO) {
        Note note = noteMapper.getNoteById(noteDTO.getNoteId());
        note.setNoteTitle(noteDTO.getNoteTitle());
        note.setNoteDescription(noteDTO.getNoteDescription());
        return noteMapper.updateNote(note);

    }

    public int deleteNote(Integer noteId) {
        return noteMapper.deleteNote(noteId);
    }
}
