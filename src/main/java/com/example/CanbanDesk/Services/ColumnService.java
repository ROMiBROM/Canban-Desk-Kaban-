package com.example.CanbanDesk.Services;

import com.example.CanbanDesk.Models.Columnn;
import com.example.CanbanDesk.Repositories.ColumnRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ColumnService {
    private final ColumnRepository columnRepository;

    public void SaveColumn(Columnn column)
    {
        columnRepository.save(column);
    }
    public Columnn findByID(Integer id)
    {
        return columnRepository.findById(id).get();
    }
    public void DeleteColumn(Columnn column)
    {
        columnRepository.delete(column);
    }
}
