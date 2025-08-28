package com.freedom.tareas.Controller;

import com.freedom.tareas.Model.Task;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Controller
@RequestMapping("/")
public class TaskController {

    private final List<Task> taskList = new ArrayList<>();
    private final AtomicLong nextId = new AtomicLong(1);

    @PostConstruct
    public void init() {
        taskList.add(new Task(nextId.getAndIncrement(), "Diseñar UI Login",
                "Crear mockups y prototipo para la pantalla de login.", LocalDate.now().plusDays(3), "Alta",
                "Pendiente"));
        taskList.add(new Task(nextId.getAndIncrement(), "Configurar Spring Security",
                "Implementar autenticación y autorización básica.", LocalDate.now().plusWeeks(1), "Media",
                "Pendiente"));
        taskList.add(new Task(nextId.getAndIncrement(), "Desarrollar CRUD Tareas",
                "Implementar las operaciones de Crear, Leer, Actualizar y Eliminar para tareas.",
                LocalDate.now().plusWeeks(2), "Baja", "En Proceso"));
    }

    @GetMapping("/")
    public String homePage(Model model, HttpServletRequest request) {
        model.addAttribute("totalTasksCount", taskList.size());
        model.addAttribute("completedTasksCount",
                taskList.stream().filter(t -> "Completada".equalsIgnoreCase(t.getStatus())).count());
        model.addAttribute("pendingTasksCount",
                taskList.stream().filter(t -> !"Completada".equalsIgnoreCase(t.getStatus())).count());
        model.addAttribute("currentUri", request.getRequestURI());
        return "index";
    }

    @GetMapping("/tasks")
    public String listTasks(Model model, HttpServletRequest request) {
        model.addAttribute("tasks", new ArrayList<>(taskList));
        model.addAttribute("currentUri", request.getRequestURI());
        return "tasks";
    }

    @GetMapping("/tasks/add")
    public String showAddTaskForm(Model model, HttpServletRequest request) {
        model.addAttribute("taskFormObject", new Task());
        model.addAttribute("currentUri", request.getRequestURI());
        return "add-task";
    }

    @PostMapping("/tasks/save")
    public String saveTask(@Valid @ModelAttribute("taskFormObject") Task task,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes,
                           Model model,
                           HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("currentUri", "/tasks/add");
            return "add-task";
        }
        task.setId(nextId.getAndIncrement());
        taskList.add(task);
        redirectAttributes.addFlashAttribute("successMessage", "¡Tarea agregada exitosamente!");
        return "redirect:/tasks";
    }

    @PostMapping("/tasks/delete/{id}")
    public String deleteTask(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        boolean removed = taskList.removeIf(task -> task.getId().equals(id));
        if (removed) {
            redirectAttributes.addFlashAttribute("successMessage", "¡Tarea eliminada exitosamente!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al eliminar: Tarea no encontrada con ID " + id);
        }
        return "redirect:/tasks";
    }

    @GetMapping("/login")
    public String loginPage(Model model, HttpServletRequest request) {
        model.addAttribute("currentUri", request.getRequestURI());
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            RedirectAttributes redirectAttributes
    ) {
        if ("usuario".equals(username) && "123".equals(password)) {
            redirectAttributes.addFlashAttribute("successMessage", "¡Bienvenido " + username + "!");
            return "redirect:/";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Usuario o contraseña incorrectos.");
            return "redirect:/login?error";
        }
    }

    @PostMapping("/tasks/update")
    public String updateTask(
            @Valid @ModelAttribute Task task,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al actualizar tarea. Por favor, verifica los campos.");
            return "redirect:/tasks";
        }

        Optional<Task> existingTaskOptional = taskList.stream()
                .filter(t -> t.getId() != null && t.getId().equals(task.getId()))
                .findFirst();

        if (existingTaskOptional.isPresent()) {
            Task existingTask = existingTaskOptional.get();
            existingTask.setTitle(task.getTitle());
            existingTask.setDescription(task.getDescription());
            existingTask.setDueDate(task.getDueDate());
            existingTask.setPriority(task.getPriority());
            existingTask.setStatus(task.getStatus());

            redirectAttributes.addFlashAttribute("successMessage", "¡Tarea actualizada exitosamente!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al actualizar: Tarea no encontrada con ID " + task.getId());
        }

        return "redirect:/tasks";
    }
}