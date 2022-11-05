package com.jkucharski.studentnotes;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import static androidx.room.OnConflictStrategy.REPLACE;

import java.util.List;

@Dao
public interface NoteDao {
    @Insert(onConflict = REPLACE)
    void insert(NoteDC noteDC);

    @Delete
    void delete(NoteDC noteDC);

    @Delete
    void reset(List<NoteDC> noteDC);

    @Query("UPDATE note_list SET name = :sName WHERE ID = :sID")
    void updateName(int sID,String sName);

    @Query("UPDATE note_list SET content = :sContent WHERE ID = :sID")
    void updateContent(int sID,String sContent);

    @Query("UPDATE note_list SET active = :sActive WHERE ID = :sID")
    void hide(int sID,boolean sActive);

    @Query("SELECT * FROM note_list WHERE subject = :sSubject AND active = 1")
    List<NoteDC> getAll(int sSubject);
}
