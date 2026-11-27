# GH-600 Domain 1: Agent Architecture & SDLC Pattern Guide

> **Target Profile:** `defconradio`  
> **Standard:** GH-600: Developing in Agentic AI Systems — Domain 1 (Prepare Agent Architecture and SDLC Processes)  
> **Applicable Repositories:**  
> - `defconradio/bip-frost-dkg` (Python 3.12 Normative Cryptographic Reference)
> - `defconradio/KMP-scaffold` (Kotlin Multiplatform Clean Architecture / MVVM)

---

## 1. Domain 1 Core Philosophy: Planning vs. Execution

In the GH-600 SDLC specification, agents operate inside strict, GitHub-native accountability boundaries:
- **Planning is reviewable intent:** Scopes the task, identifies bounded inputs/outputs, sets autonomy level, and establishes validation criteria before modifying code.
- **Execution changes state:** Operates on an isolated feature branch, adheres to scoped instructions, runs local validation tests, and presents evidence in a Pull Request.
- **Human judgment gates merge:** Protected branches and rulesets prevent automated or unreviewed commits from directly hitting production (`master`/`main`).

---

## 2. SDLC Pattern for Future Tasks (8-Step Cycle)

For every new task or feature given to an AI agent:

```
[1. Scoped Issue/Prompt]
         │
         ▼
[2. Isolated Feature Branch] (never work on master)
         │
         ▼
[3. Durable Plan Artifact] (docs/plans/<task>-plan.md)
         │
         ▼
[4. Implementation] (bounded by .github/copilot-instructions.md)
         │
         ▼
[5. Local Validation] (all.sh / ./gradlew test)
         │
         ▼
[6. Pull Request with Template] (Risk notes + Evidence checklist)
         │
         ▼
[7. Automated CI & Branch Ruleset] (Concurrency control + Status check gate)
         │
         ▼
[8. Human Review & Merge] (Accountability preserved in Git history)
```

### Detailed Breakdown

1. **Task Definition:** Define clear success criteria and bounded scope. Specify autonomy level (Low: read-only/plan; Medium: edit/test/PR; High: MCP/workflow modification).
2. **Branch Isolation:** Always branch off default:
   ```bash
   git checkout -b feat/<task-name>
   ```
3. **Durable Plan Creation:** For non-trivial tasks, author and commit a plan under `docs/plans/<task-name>-plan.md` defining:
   - Goal & scope
   - Boundaries (files to touch, forbidden files)
   - Step-by-step implementation sequence
   - Exact validation criteria
4. **Implementation with Guardrails:**
   - Obey repository-wide `.github/copilot-instructions.md`.
   - Obey path-specific `.github/instructions/*.instructions.md`.
   - Never commit secrets, keystores, `local.properties`, or `*.scp` files.
5. **Local Validation:**
   - Python: Run `./all.sh` (or `pytest -v`, `ruff check`, `mypy`).
   - KMP: Run `./gradlew :shared:assemble :shared:jvmTest`.
6. **Pull Request with Template:**
   - Push branch to remote.
   - PR description must populate from `.github/pull_request_template.md`.
   - Fill in explicit **Risk Notes** (normative code touched, API changes, target platforms, dependency changes) and check off **Validation Evidence**.
7. **CI Concurrency & Ruleset Gating:**
   - Top-level workflow concurrency cancels obsolete intermediate runs (`cancel-in-progress: true`).
   - GitHub branch ruleset blocks direct push and requires status check + 1 human review approval.
8. **Human Review & Merge:**
   - Reviewer validates diff, tests, and risk notes before squash/merge.

---

## 3. Standard File Structure for Domain 1 Scaffold

When bootstrapping Domain 1 in any repository, establish this uniform layout:

```text
<repository-root>/
├── .github/
│   ├── copilot-instructions.md                 # Universal repo guardrails & standards
│   ├── pull_request_template.md                # PR gate: Risk Notes & Evidence checklists
│   ├── instructions/
│   │   └── <domain>.instructions.md            # Path-scoped rules (applyTo: glob pattern)
│   └── workflows/
│       └── <ci-workflow>.yml                   # Concurrency + permissions: contents: read
├── docs/
│   ├── plans/
│   │   └── <task>-plan.md                      # Durable reviewable intent before edits
│   └── protect-master-ruleset.json             # Branch ruleset policy schema
├── SDLC.scp                                    # Architecture & SDLC map
└── README.md                                   # Project documentation
```

---

## 4. Concrete Reference Implementations

### A. Python Cryptographic Repository (`bip-frost-dkg`)

* **Repository Instructions (`.github/copilot-instructions.md`):**
  - Forbids modifying normative reference code (`python/chilldkg_ref/`) and test vectors (`vectors/`).
  - Requires validation via `./all.sh` or `pytest`.
  - Mandates draft PRs with risk notes; forbids direct pushes to `master`.
* **Path Instructions (`.github/instructions/python-examples.instructions.md`):**
  - Applies to `python/**`.
  - Requires 2-of-3 threshold scenarios with realistic participant names (`Alice`, `Bob`, `Peter`).
  - Requires unit tests covering happy-path, recovery, and faulty-party blame.
* **Status Check Gate:** `validate-chilldkg`.

### B. Kotlin Multiplatform Repository (`KMP-scaffold`)

* **Repository Instructions (`.github/copilot-instructions.md`):**
  - Defines Clean Architecture + MVVM, Koin DI, Ktor, Napier, and Compose Multiplatform.
  - Forbids committing signing keys, `local.properties`, credentials, or `*.scp` files.
  - Enforces domain purity (pure Kotlin without Android/Ktor dependencies).
* **Path Instructions (`.github/instructions/kmp-architecture.instructions.md`):**
  - Applies to `{shared/**,composeApp/**}`.
  - Enforces pure Kotlin domain use-cases and immutable `StateFlow<UiState>` in ViewModels.
* **Status Check Gate:** `build`.

---

## 5. GitHub Ruleset Schema (`docs/protect-master-ruleset.json`)

Deploy this ruleset to GitHub to enforce Domain 1 branch protection:

```json
{
  "name": "Protect master from unreviewed agent merges",
  "target": "branch",
  "enforcement": "active",
  "conditions": {
    "ref_name": {
      "include": [
        "refs/heads/master"
      ],
      "exclude": []
    }
  },
  "rules": [
    {
      "type": "deletion"
    },
    {
      "type": "non_fast_forward"
    },
    {
      "type": "pull_request",
      "parameters": {
        "required_approving_review_count": 1,
        "dismiss_stale_reviews_on_push": true,
        "require_code_owner_review": false,
        "require_last_push_approval": true,
        "required_review_thread_resolution": true
      }
    },
    {
      "type": "required_status_checks",
      "parameters": {
        "strict_required_status_checks_policy": true,
        "required_status_checks": [
          {
            "context": "build"
          }
        ]
      }
    }
  ],
  "bypass_actors": []
}
```

---

## 6. How to Send Up Domain 1 to GitHub (Step-by-Step)

### Step 1: Create Branch & Add Artifacts
```bash
git checkout -b feat/domain-1-agent-architecture-sdlc
git add .github/ docs/
git commit -m "chore: scaffold GH-600 domain 1 instructions, plan, CI, and ruleset"
```

### Step 2: Push Feature Branch
```bash
git push -u origin feat/domain-1-agent-architecture-sdlc
```

### Step 3: Open Pull Request
Navigate to:
```text
https://github.com/defconradio/<repo>/compare/master...feat/domain-1-agent-architecture-sdlc
```
Copy and paste the `.github/pull_request_template.md` content into the PR description, fill out the Risk Notes and Validation Evidence, and submit.

### Step 4: Configure GitHub Ruleset
1. Open `https://github.com/defconradio/<repo>/settings/rules`
2. Click **New ruleset** $\rightarrow$ **New branch ruleset**.
3. Name: `Protect master from unreviewed agent merges`.
4. Enforcement: **Active**. Target: **Default branch** (`refs/heads/master`).
5. Check:
   - Restrict deletions
   - Block force pushes
   - Require a pull request before merging (1 review, dismiss stale approvals, require last push approval)
   - Require status checks to pass (`build` or `validate-chilldkg`)
6. Save changes.
