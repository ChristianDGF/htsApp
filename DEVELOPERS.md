Developers Guide — htsApp

Overview
- Spring Boot application using Thymeleaf for server-side views. Tailwind classes are used in templates for modern look; there is no automated Tailwind build pipeline in the repo by default.

Project layout (important folders)
- src/main/java/com/hts/htsApp
  - controller — MVC controllers (ContentController, EmpresaController, UserController/UsuarioController, ...)
  - service — business logic
  - repo — Spring Data JPA repositories
  - model — JPA entities
  - security — security configuration and role management
  - config — MVC and other Spring configs (e.g., MvcConfig.java)
- src/main/resources/templates — Thymeleaf templates (organized into admin, contents, plantilla, etc.)
- src/main/resources/static — static assets (images, css). Place compiled Tailwind CSS in static/css.
- src/main/resources/application.properties — runtime configuration

Thymeleaf conventions used
- Most templates use th:fragment for common parts. The navbar fragment is in plantilla/navbar.html.
- Use th:object on forms and th:field on inputs so Spring can bind values back to the model.
- Prefer POST-Redirect-GET for form submissions (controller redirects after successful save).

Tailwind notes
- Templates already use Tailwind utility classes. If you want a production pipeline, add a build step (npm + tailwindcss) and commit generated CSS to src/main/resources/static/css/main.css, then include it in templates.
- For local changes without a build pipeline, you can add a CDN link to Tailwind in the main layout for quick styling (not recommended for production).

Common development tasks
- Add a new entity: create model, repo, service, controller, and Thymeleaf templates (list, form, view).
- To add a menu item in the admin home: edit templates/admin/home.html and plantilla/navbar.html.

Where to add views
- Admin UI: src/main/resources/templates/admin
- Content-specific UI: src/main/resources/templates/contents
- Shared fragments: src/main/resources/templates/plantilla

Security and roles
- The app uses role-based access (ADMIN role required for admin pages). See security package for configuration.
- Controller methods should be annotated or protected by Security config; check existing controllers for examples.

Debugging and tests
- Enable SQL logging in application.properties (spring.jpa.show-sql=true) to inspect queries.
- Use integration tests under src/test/java to validate controllers and repos.

If you want, I can scan the codebase to produce a fully accurate model/controller map and add code snippets for creating a new CRUD entity.
