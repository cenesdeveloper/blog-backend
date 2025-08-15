# Blog Application – Backend

A full-stack blogging platform with a Spring Boot backend, PostgreSQL database, and React frontend. Supports post creation, category/tag filtering, draft management, and responsive design.

> **Frontend Repo:** https://github.com/cenesdeveloper/blog-frontend  
> **Frontend Live Demo:** https://blog-frontend-green-five.vercel.app/

---

## Features
- **PostgreSQL Database:** Stores all blog data, including posts, categories, tags, and drafts.
- **Spring Boot Backend:** RESTful API for managing blog content. Includes filtering by category/tag, draft management, and secure CRUD operations for authenticated users.
- **React Frontend:** Responsive interface for browsing, creating, and managing blog posts. Supports category/tag filtering, draft management, and detailed post views.

---

## Prerequisites
- Java 17 or higher  
- Node.js and npm  
- PostgreSQL  
- IDE (IntelliJ IDEA, Eclipse, VS Code, etc.)

---

## Installation

### Backend Setup
1. Clone this repository.  
2. Open the `src/main/resources/application.properties` file and enter your PostgreSQL database credentials.  
3. Run the Spring Boot application.

### Frontend Setup
1. Clone the frontend repository.  
2. Run `npm install` to install the necessary dependencies.  
3. Update `vercel.json` with the appropriate backend API URL if needed.  
4. Run `npm start` to start the React application.

---

## Usage
- Access the frontend via `http://localhost:3000`  
- Access the backend API via `http://localhost:8080`

---

## API Endpoints

**Base path:** `/api/v1`

### Posts
- `GET /posts` — List published posts (optional filters: `categoryId`, `tagId`)
- `GET /posts/{id}` — Get a single post (drafts visible to owner)
- `GET /posts/drafts` — List drafts for logged-in user
- `POST /posts` — Create a new post (requires category and authentication)
- `PUT /posts/{id}` — Update a post (owner/admin only)
- `DELETE /posts/{id}` — Delete a post (owner/admin only)

### Categories
- `GET /categories` — List all categories
- `POST /categories` — Create a new category
- `DELETE /categories/{id}` — Delete a category  
  *Note: Editing/renaming categories is not supported.*

### Tags
- `GET /tags` — List all tags
- `POST /tags` — Create one or more tags (bulk create)
- `DELETE /tags/{id}` — Delete a tag  
  *Note: Editing/renaming tags is not supported.*
