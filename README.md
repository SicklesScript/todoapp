# University TODO Application

A simple TODO web app for coursework planning.  
It supports task creation, completion tracking, filtering, sorting, and browser local storage persistence.

## Features

- Add tasks with:
  - Title
  - Course code
  - Due date
  - Priority (High / Medium / Low)
- Mark tasks as completed
- Delete individual tasks
- Clear all completed tasks
- Search by title or course
- Filter by status (All / Active / Completed)
- Sort by due date, priority, or created time
- Save tasks in `localStorage`

## Project Structure

- `index.html` - App layout
- `styles.css` - Styling and responsive layout
- `app.js` - Task logic and storage

## How to Run

Since this is a static app, you can run it in either of these ways:

1. Open `index.html` directly in your browser, or
2. Use a local development server (recommended), for example:

```bash
python3 -m http.server 8000
```

Then open [http://localhost:8000](http://localhost:8000).

## Suggested Course Extensions

- Add user authentication
- Add backend API and database
- Add drag-and-drop ordering
- Add category tags and progress charts
- Add unit tests for core task functions
