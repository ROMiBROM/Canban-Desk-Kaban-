package com.example.CanbanDesk.Services;

import com.example.CanbanDesk.Models.Task;
import com.example.CanbanDesk.Repositories.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    public Task SaveTask(Task task)
    {
        return taskRepository.save(task);
    }
    public Task findByID(Integer id)
    {
        return taskRepository.findById(id).get();
    }
    public List<Task> findAllTaskBycolumnID(Integer id)
    {
       return taskRepository.findAllByColumnId(id);
    }
    public void DeleteTask(Task task)
    {
        taskRepository.delete(task);
    }

    public List<Task> findAll()
    {
        return taskRepository.findAll();
    }

    public List<Task> SortByPriorityByColumn(Integer id)
    {
        List<Task> tasks = taskRepository.findAllByColumnId(id);
        String[] priorities = {"Высокий", "Средний", "Низкий"};
        tasks.sort(Comparator.comparingInt(task -> Arrays.asList(priorities).indexOf(task.getPriority())));
        return tasks;
    }
}
