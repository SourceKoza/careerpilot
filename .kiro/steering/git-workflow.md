# Git Workflow Rules

## Branching Strategy

- The **default base branch** is `develop` (not `main`).
- Always create feature branches from `origin/develop`.
- Before creating a new branch, run `git fetch origin develop` to ensure it's up to date.
- Branch naming convention: `feature/sprint-XX-short-description`

## Branch Creation Steps

1. `git fetch origin develop`
2. `git checkout -b feature/<branch-name> origin/develop`
3. Implement changes
4. Commit with conventional commit messages
5. `git push -u origin feature/<branch-name>`

## Commit Rules

- Use conventional commits: `feat(sprint-XX): description`
- Keep Sprint work in separate commits when multiple sprints are delivered together.
- Stage only relevant files — avoid `git add .`

## PR Target

- All PRs target `develop`, never `main`.
- `main` is only updated via release merges from `develop`.
