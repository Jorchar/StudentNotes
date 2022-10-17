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
    void update(int sID,String sName);

    @Query("SELECT * FROM note_list WHERE subject = :sSubject")
    List<NoteDC> getAll(int sSubject);
}
