package com.freedom.tareas.Model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Data // Genera getters, setters, toString, equals, hashCode
@NoArgsConstructor // Genera constructor sin argumentos
@AllArgsConstructor // Genera constructor con todos los argumentos
public class Task {

    private Long id;

    @NotBlank(message = "El título no puede estar vacío.")
    @Size(min = 3, max = 100, message = "El título debe tener entre 3 y 100 caracteres.")
    private String title;

    @NotBlank(message = "La descripción no puede estar vacía.")
    @Size(max = 500, message = "La descripción no puede exceder los 500 caracteres.")
    private String description;

    @NotNull(message = "La fecha de vencimiento es obligatoria.")
    @DateTimeFormat(pattern = "yyyy-MM-dd") 
   private LocalDate dueDate;

    @NotBlank(message = "La prioridad no puede estar vacía.")
    private String priority; // Ej: "Alta", "Media", "Baja"

    @NotBlank(message = "El estado no puede estar vacío.")
    private String status; // Ej: "Pendiente", "En Proceso", "Completada"


    public Task(String title, String description, LocalDate dueDate, String priority, String status) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.priority = priority;
        this.status = status;
    }
}