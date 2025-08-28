// Espera a que el DOM esté completamente cargado
document.addEventListener('DOMContentLoaded', function() {
    const loginForm = document.getElementById('loginForm');

    // Asegura que el formulario de login exista en la página
    if (loginForm) {
        // Agrega un "escuchador" al evento 'submit' del formulario
        loginForm.addEventListener('submit', function(event) {

            // Obtiene los elementos de usuario, contraseña y el área de alerta
            const usernameInput = document.getElementById('username');
            const passwordInput = document.getElementById('password');
            const loginAlert = document.getElementById('loginAlert');

            const username = usernameInput.value.trim();
            const password = passwordInput.value.trim();

            // Realiza la validación del lado del cliente (campos obligatorios)
            let isValidClient = true;
            let clientErrorMessage = '';

            if (username === '') {
                usernameInput.classList.add('is-invalid');
                clientErrorMessage += 'El nombre de usuario es obligatorio.<br>';
                isValidClient = false;
            }

            if (password === '') {
                passwordInput.classList.add('is-invalid');
                clientErrorMessage += 'La contraseña es obligatoria.<br>';
                isValidClient = false;
            }

            // Si la validación del cliente falla, previene el envío y muestra el error
            if (!isValidClient) {
                event.preventDefault(); // Detener el envío del formulario
                if (loginAlert && clientErrorMessage) {
                    loginAlert.style.display = 'block';
                    loginAlert.classList.remove('alert-success');
                    loginAlert.classList.add('alert-danger');
                    loginAlert.innerHTML = `<i class="fas fa-exclamation-triangle"></i> ${clientErrorMessage}`;
                }
                return; // Detiene la ejecución si hay errores
            }

            // Si la validación del cliente pasa, el formulario se envía al servidor.
        });
    }
});