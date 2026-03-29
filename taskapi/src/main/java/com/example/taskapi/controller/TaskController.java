package com.example.taskapi.controller;

import com.example.taskapi.model.Task;
import com.example.taskapi.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Task controller — handles all HTTP requests for /api/tasks.
 *
 * Django equivalent:
 *   class TaskViewSet(viewsets.ModelViewSet):
 *       queryset = Task.objects.all()
 *       serializer_class = TaskSerializer
 *
 * But in Spring, you wire things manually — more code, more control.
 *
 * Key annotations:
 * - @RestController = "I handle HTTP and return JSON"
 * - @RequestMapping = "base URL path" (like router.register in DRF)
 * - @GetMapping, @PostMapping, etc. = specific HTTP methods
 * - @RequestBody = "parse the JSON body into this Java object" (like serializer.data)
 * - @PathVariable = "extract {id} from the URL" (like <int:pk> in Django URLs)
 *
 * The constructor takes TaskService as a parameter. Spring automatically
 * provides it — this is Dependency Injection. In Django, you'd just
 * import and call Task.objects directly. Here, Spring manages the wiring.
 */
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    // Constructor injection — Spring sees TaskService is needed and provides it.
    // Django equivalent: there isn't one. Django uses imports instead of DI.
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * GET /api/tasks — list all tasks
     * Django: def list(self, request): return Response(TaskSerializer(tasks, many=True).data)
     */
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    /**
     * GET /api/tasks/{id} — get one task
     * Django: def retrieve(self, request, pk): task = get_object_or_404(Task, pk=pk)
     */
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id)
                .map(ResponseEntity::ok)                           // found → 200
                .orElse(ResponseEntity.notFound().build());        // not found → 404
    }

    /**
     * POST /api/tasks — create a new task
     * Django: def create(self, request): serializer.save()
     */
    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        Task created = taskService.createTask(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);  // 201
    }

    /**
     * PUT /api/tasks/{id} — update a task
     * Django: def update(self, request, pk): serializer.save()
     */
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task task) {
        return taskService.updateTask(id, task)
                .map(ResponseEntity::ok)                           // updated → 200
                .orElse(ResponseEntity.notFound().build());        // not found → 404
    }

    /**
     * DELETE /api/tasks/{id} — delete a task
     * Django: def destroy(self, request, pk): task.delete()
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        if (taskService.deleteTask(id)) {
            return ResponseEntity.noContent().build();             // deleted → 204
        }
        return ResponseEntity.notFound().build();                  // not found → 404
    }
}
