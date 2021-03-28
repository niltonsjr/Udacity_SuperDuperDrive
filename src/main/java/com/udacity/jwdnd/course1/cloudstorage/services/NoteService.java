package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    private final NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public int insertNote(Note note) {
        return noteMapper.insertNote(note);
    }

    public List<Note> getAllNotes(int userId) {
        return noteMapper.getAllNotes(userId);
    }

    public Note getNotById(int noteId) {
        return noteMapper.getNoteById(noteId);
    }

    public void updateNote(Note note) {
            noteMapper.updateNote(note);
    }

    public void deleteNote(int id) {
            noteMapper.deleteNote(id);
    }
}
