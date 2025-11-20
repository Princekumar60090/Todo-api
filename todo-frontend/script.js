// --- STATE MANAGEMENT ---
// These variables will hold the user's login state.
let currentUser = null;
let authToken = null;

// --- API CONFIGURATION ---
const API_BASE_URL = 'https://todo-api-backend-e3mg.onrender.com/';

// --- UI ELEMENTS ---
const authView = document.getElementById('auth-view');
const appView = document.getElementById('app-view');
const loginForm = document.getElementById('login-form');
const registerForm = document.getElementById('register-form');
const showRegisterLink = document.getElementById('show-register-link');
const showLoginLink = document.getElementById('show-login-link');
const welcomeMessage = document.getElementById('welcome-message');
const logoutBtn = document.getElementById('logout-btn');
const taskInput = document.getElementById('task-input');
const addTaskBtn = document.getElementById('add-task-btn');
const todoList = document.getElementById('todo-list');
const loginFormContainer = document.getElementById('login-form-container');
const registerFormContainer = document.getElementById('register-form-container');

// --- UI LOGIC ---

/**
 * Shows the specified view ('auth' or 'app') and hides the other.
 * @param {string} viewName - The name of the view to show.
 */
function showView(viewName) {
    authView.classList.toggle('hidden', viewName !== 'auth');
    appView.classList.toggle('hidden', viewName !== 'app');
}

/**
 * Renders the list of to-do items on the page.
 * @param {Array} todos - An array of to-do item objects.
 */
function renderTodos(todos) {
    todoList.innerHTML = ''; // Clear existing list
    todos.forEach(todo => {
        const li = document.createElement('li');
        li.dataset.id = todo.id; // Store ID for future actions
        li.classList.toggle('completed', todo.completed);

        li.innerHTML = `
            <input type="checkbox" class="complete-checkbox" ${todo.completed ? 'checked' : ''}>
            <span class="task-text">${todo.task}</span>
            <div class="actions">
                <button class="edit-btn">✏️</button>
                <button class="delete-btn">🗑️</button>
            </div>
        `;
        todoList.appendChild(li);
    });
}

// --- API CALLS ---

async function register(username, password) {
    try {
        const response = await fetch(`${API_BASE_URL}/public/create-user`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, password }),
        });

        // --- THIS IS THE MODIFIED PART ---
        if (response.ok) { // response.ok is true for 201 CREATED
            alert('Registration successful! Please log in.');
            showLoginLink.click();
        } else if (response.status === 409) { // 409 CONFLICT
            alert('User already exists. Please try a different username.');
        } else {
            // All other errors
            throw new Error('Registration failed. Please try again.');
        }
        // --- END OF MODIFIED PART ---

    } catch (error) {
        alert(error.message);
    }
}

async function login(username, password) {
    try {
        // Create the Basic Auth token
        const token = 'Basic ' + btoa(`${username}:${password}`);
        // Test the token by trying to fetch todos
        const response = await fetch(`${API_BASE_URL}/todos`, {
            headers: { 'Authorization': token }
        });
        if (!response.ok) throw new Error('Login failed. Please check your username and password.');

        // If successful, save the state
        currentUser = username;
        authToken = token;

        // Update UI
        welcomeMessage.textContent = `Welcome, ${currentUser}!`;
        showView('app');
        fetchTodos();
    } catch (error) {
        alert(error.message);
    }
}

async function fetchTodos() {
    try {
        const response = await fetch(`${API_BASE_URL}/todos`, {
            headers: { 'Authorization': authToken }
        });
        if (!response.ok) throw new Error('Could not fetch tasks.');
        const todos = await response.json();
        renderTodos(todos);
    } catch (error) {
        alert(error.message);
    }
}

async function addTodo(taskText) {
    try {
        const response = await fetch(`${API_BASE_URL}/todos`, {
            method: 'POST',
            headers: {
                'Authorization': authToken,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ task: taskText, completed: false })
        });
        if (!response.ok) throw new Error('Could not add task.');
        taskInput.value = '';
        fetchTodos(); // Refresh list
    } catch (error) {
        alert(error.message);
    }
}

async function updateTodo(id, updatedTask, isCompleted) {
    try {
        const response = await fetch(`${API_BASE_URL}/todos/${id}`, {
            method: 'PUT',
            headers: {
                'Authorization': authToken,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ task: updatedTask, completed: isCompleted })
        });
        if (!response.ok) throw new Error('Could not update task.');
        fetchTodos();
    } catch (error) {
        alert(error.message);
    }
}

async function deleteTodo(id) {
    try {
        const response = await fetch(`${API_BASE_URL}/todos/${id}`, {
            method: 'DELETE',
            headers: { 'Authorization': authToken }
        });
        // The fix: check for 204 No Content status
        if (!response.ok && response.status !== 204) {
            throw new Error('Could not delete task.');
        }
        fetchTodos();
    } catch (error) {
        alert(error.message);
    }
}

// --- EVENT LISTENERS ---

showRegisterLink.addEventListener('click', (e) => {
    e.preventDefault();
    loginFormContainer.classList.add('hidden');
    registerFormContainer.classList.remove('hidden');
});

showLoginLink.addEventListener('click', (e) => {
    e.preventDefault();
    registerFormContainer.classList.add('hidden');
    loginFormContainer.classList.remove('hidden');
});

registerForm.addEventListener('submit', (e) => {
    e.preventDefault();
    const username = document.getElementById('register-username').value;
    const password = document.getElementById('register-password').value;
    register(username, password);
});

loginForm.addEventListener('submit', (e) => {
    e.preventDefault();
    const username = document.getElementById('login-username').value;
    const password = document.getElementById('login-password').value;
    login(username, password);
});

logoutBtn.addEventListener('click', () => {
    currentUser = null;
    authToken = null;
    showView('auth');
});

addTaskBtn.addEventListener('click', () => {
    const taskText = taskInput.value.trim();
    if (taskText) {
        addTodo(taskText);
    }
});

todoList.addEventListener('click', (e) => {
    const li = e.target.closest('li');
    if (!li) return;

    const id = li.dataset.id;
    const taskTextElement = li.querySelector('.task-text');
    const isCompleted = li.querySelector('.complete-checkbox').checked;

    // Handle checkbox click to complete/uncomplete task
    if (e.target.classList.contains('complete-checkbox')) {
        updateTodo(id, taskTextElement.textContent, e.target.checked);
    }

    // Handle delete button click
    if (e.target.classList.contains('delete-btn')) {
        if (confirm('Are you sure you want to delete this task?')) {
            deleteTodo(id);
        }
    }

    // Handle edit button click
    if (e.target.classList.contains('edit-btn')) {
        const newText = prompt('Edit your task:', taskTextElement.textContent);
        if (newText && newText.trim() !== '') {
            updateTodo(id, newText.trim(), isCompleted);
        }
    }
});

// --- INITIALIZATION ---
// Start with the authentication view

showView('auth');

