package com.jkucharski.studentnotes;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import static androidx.room.OnConflictStrategy.REPLACE;

import java.util.List;

@Dao
public interface SubjectDao {
    @Insert(onConflict = REPLACE)
    void insert(SubjectDC subjectDC);

    @Delete
    void delete(SubjectDC subjectDC);

    @Delete
    void reset(List<SubjectDC> subjectDC);

    @Query("UPDATE subject_list SET name = :sName AND description = :sDesc WHERE ID = :sID")
    void update(int sID,String sName,String sDesc);

    @Query("SELECT * FROM subject_list")
    List<SubjectDC> getAll();
}
