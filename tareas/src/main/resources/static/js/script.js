// Funcionalidades JavaScript para la aplicación de tareas

// Variables para simulación de datos en el frontend (si no se usa backend)
let tasks = [];
let nextTaskId = 1;

// Función para inicializar los datos de tareas (uso potencial con/sin backend)
function initializeTasks(tasksFromBackend) {
    if (tasksFromBackend && tasksFromBackend.length > 0) {
        tasks = tasksFromBackend;
        const maxId = tasks.reduce((max, task) => task.id > max ? task.id : max, 0);
        nextTaskId = maxId + 1;
    } else {
        // Simulación de datos si no vienen del backend
        tasks = [
            // ... (añadir tareas simuladas aquí si es necesario)
        ];
    }
    // Comentado: renderTasks(); // Llama a renderizar si la tabla es controlada por JS
}

// Prepara un modal de formulario para agregar tarea (si se usa un flujo de modal)
function prepareAddTaskFormModal() {
    $('#taskFormModalLabel').html('<i class="fas fa-plus-circle"></i> Agregar Nueva Tarea');
    $('#taskForm').attr('action', '/tasks/add');
    $('#taskForm')[0].reset();
    $('#taskId').val('');
}

// Prepara un modal de formulario para editar tarea (si se usa un flujo de modal)
function prepareEditTaskFormModal(id, title, description, dueDate, priority) {
    $('#taskFormModalLabel').html('<i class="fas fa-edit"></i> Editar Tarea');
    $('#taskForm').attr('action', '/tasks/update/' + id);
    $('#taskId').val(id);
    $('#taskTitle').val(title);
    $('#taskDescription').val(description);
    $('#taskDueDate').val(dueDate);
    $('#taskPriority').val(priority);
}

// Manejo del envío del formulario de tarea (Agregar/Editar) para validación simple
$('#taskForm').submit(function(event) {
    let isValid = true;
    $(this).find('[required]').each(function() {
        if ($(this).val() === '') {
            isValid = false;
            $(this).addClass('is-invalid');
        } else {
            $(this).removeClass('is-invalid');
        }
    });

    if (!isValid) {
        event.preventDefault();
    }
});

// Prepara el modal de confirmación para eliminar una tarea
function prepareDeleteTaskModal(taskId, taskTitle) {
    $('#taskToDeleteName').text(taskTitle);
    $('#deleteTaskForm').attr('action', '/tasks/delete/' + taskId);
}

// Comentado: Lógica alternativa de eliminación o inicialización al cargar el documento
// $(document).ready(function() {
// initializeTasks();
// Considerar apertura de modales con data-attributes si aplica
// });