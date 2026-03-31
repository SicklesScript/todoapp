const STORAGE_KEY = "university.todo.tasks";

const priorityRank = {
  High: 1,
  Medium: 2,
  Low: 3,
};

const elements = {
  form: document.getElementById("task-form"),
  titleInput: document.getElementById("task-title"),
  courseInput: document.getElementById("task-course"),
  dueDateInput: document.getElementById("task-due-date"),
  priorityInput: document.getElementById("task-priority"),
  searchInput: document.getElementById("search-input"),
  statusFilter: document.getElementById("status-filter"),
  sortSelect: document.getElementById("sort-select"),
  clearCompletedBtn: document.getElementById("clear-completed-btn"),
  taskList: document.getElementById("task-list"),
};

/** @type {Array<{id: string, title: string, course: string, dueDate: string, priority: "High"|"Medium"|"Low", completed: boolean, createdAt: number}>} */
let tasks = loadTasks();

elements.form.addEventListener("submit", handleCreateTask);
elements.searchInput.addEventListener("input", renderTasks);
elements.statusFilter.addEventListener("change", renderTasks);
elements.sortSelect.addEventListener("change", renderTasks);
elements.clearCompletedBtn.addEventListener("click", clearCompletedTasks);

renderTasks();

function handleCreateTask(event) {
  event.preventDefault();

  const title = elements.titleInput.value.trim();
  const course = elements.courseInput.value.trim().toUpperCase();
  const dueDate = elements.dueDateInput.value;
  const priority = elements.priorityInput.value;

  if (!title || !course || !dueDate || !priority) {
    return;
  }

  const task = {
    id: crypto.randomUUID(),
    title,
    course,
    dueDate,
    priority,
    completed: false,
    createdAt: Date.now(),
  };

  tasks.push(task);
  saveTasks(tasks);
  elements.form.reset();
  elements.priorityInput.value = "Medium";
  renderTasks();
}

function renderTasks() {
  const query = elements.searchInput.value.trim().toLowerCase();
  const status = elements.statusFilter.value;
  const sortBy = elements.sortSelect.value;

  const filteredTasks = tasks
    .filter((task) => {
      const matchesQuery =
        task.title.toLowerCase().includes(query) ||
        task.course.toLowerCase().includes(query);

      if (status === "active") {
        return !task.completed && matchesQuery;
      }

      if (status === "completed") {
        return task.completed && matchesQuery;
      }

      return matchesQuery;
    })
    .sort((left, right) => {
      if (sortBy === "priority") {
        return priorityRank[left.priority] - priorityRank[right.priority];
      }

      if (sortBy === "createdAt") {
        return right.createdAt - left.createdAt;
      }

      return left.dueDate.localeCompare(right.dueDate);
    });

  elements.taskList.innerHTML = "";

  if (filteredTasks.length === 0) {
    const emptyEl = document.createElement("li");
    emptyEl.className = "empty-state";
    emptyEl.textContent = "No tasks found. Add one to get started.";
    elements.taskList.appendChild(emptyEl);
    return;
  }

  filteredTasks.forEach((task) => {
    elements.taskList.appendChild(createTaskItem(task));
  });
}

function createTaskItem(task) {
  const item = document.createElement("li");
  const priorityClass = `priority-${task.priority.toLowerCase()}`;
  item.className = `task-item ${priorityClass}${task.completed ? " completed" : ""}`;

  const checkbox = document.createElement("input");
  checkbox.type = "checkbox";
  checkbox.checked = task.completed;
  checkbox.ariaLabel = `Mark task ${task.title} as completed`;
  checkbox.addEventListener("change", () => toggleTask(task.id));

  const content = document.createElement("div");
  const taskTitle = document.createElement("p");
  taskTitle.className = "task-title";
  taskTitle.textContent = task.title;

  const taskMeta = document.createElement("p");
  taskMeta.className = "task-meta";
  taskMeta.textContent = `${task.course} | Due: ${formatDate(task.dueDate)} | ${task.priority}`;

  content.append(taskTitle, taskMeta);

  const actions = document.createElement("div");
  actions.className = "task-actions";

  const deleteBtn = document.createElement("button");
  deleteBtn.className = "delete-btn";
  deleteBtn.type = "button";
  deleteBtn.textContent = "Delete";
  deleteBtn.addEventListener("click", () => deleteTask(task.id));

  actions.appendChild(deleteBtn);
  item.append(checkbox, content, actions);

  return item;
}

function toggleTask(taskId) {
  tasks = tasks.map((task) =>
    task.id === taskId ? { ...task, completed: !task.completed } : task
  );
  saveTasks(tasks);
  renderTasks();
}

function deleteTask(taskId) {
  tasks = tasks.filter((task) => task.id !== taskId);
  saveTasks(tasks);
  renderTasks();
}

function clearCompletedTasks() {
  tasks = tasks.filter((task) => !task.completed);
  saveTasks(tasks);
  renderTasks();
}

function loadTasks() {
  const raw = localStorage.getItem(STORAGE_KEY);
  if (!raw) {
    return [];
  }

  try {
    const parsed = JSON.parse(raw);
    return Array.isArray(parsed) ? parsed : [];
  } catch {
    return [];
  }
}

function saveTasks(nextTasks) {
  localStorage.setItem(STORAGE_KEY, JSON.stringify(nextTasks));
}

function formatDate(dateString) {
  const date = new Date(`${dateString}T00:00:00`);
  if (Number.isNaN(date.getTime())) {
    return dateString;
  }

  return date.toLocaleDateString(undefined, {
    year: "numeric",
    month: "short",
    day: "numeric",
  });
}
