# GH-600 Domain 2: Agent Tools, MCP & Environment Interaction Guide

> **Target Profile:** `defconradio`  
> **Standard:** GH-600: Developing in Agentic AI Systems — Domain 2 (Implement Tool Use and Environment Interaction)  
> **Applicable Repositories:**  
> - `defconradio/KMP-scaffold` (Kotlin Multiplatform Clean Architecture / MVVM)  
> - `defconradio/apagar-org` (Production Kotlin Multiplatform Organization Scaffold)  
> - `defconradio/bip-frost-dkg` (Python 3.12 Normative Cryptographic Reference)  

---

## 1. Domain 2 Core Principles: Least-Privilege Tools & Environment Parity

Domain 2 assesses how autonomous agents interact with repositories, environments, external data sources, and command-line execution:
- **Custom Agent Specialization:** Split agent roles into least-privilege profiles instead of relying on one generic, all-powerful agent.
- **Environment Determinism:** Cloud agents (`/delegate`, `&`) execute in ephemeral Linux VMs without local caches. Toolchains (JDK 21, Python 3.12, Gradle wrapper, Android SDK) must be warmed deterministically.
- **Durable Skills & Prompts:** Standardize recurring diagnostic routines (Gradle resolution, stacktrace tracing) and scaffolding into versioned, testable repo assets.
- **Secure Model Context Protocol (MCP):** Connect external systems (GitHub, issue trackers) using least-privilege tool exposure and `COPILOT_MCP_` credential hygiene.

---

## 2. Custom Agent YAML Specification (`.github/agents/*.agent.md`)

Custom agent profiles live under `.github/agents/`.

### Critical Frontmatter Rules & Traps
1. `description` is **mandatory**; `name` is optional (defaults to filename).
2. `tools`:
   - Omitting `tools` **enables all tools** by default.
   - `tools: []` **disables all tools**.
3. **Repository Search vs. Web:** The `search` tool searches **local workspace files and text only**, never the public web.
4. **Shell Execution:** Grant `execute` only to implementers requiring command running; omit for reviewers.
5. **Subagent Delegation:** Grant `agent` only when the profile needs to orchestrate specialist subagents.

### Tool Privilege Matrix

| Tool | Capability | Typical Role | Least-Privilege Rule |
| :--- | :--- | :--- | :--- |
| `read` | Inspect file contents | All agents | Safe, non-destructive |
| `search` | Grep/glob workspace code | All agents | Local workspace only (not web) |
| `edit` | Write & modify files | Implementer | Omit for reviewers |
| `execute` | Shell commands (`./gradlew`) | Implementer | Omit for reviewers / planners |
| `agent` | Invoke specialist subagent | Orchestrator | Omit for leaf workers |

---

## 3. Concrete Agent Profiles

### A. Read-Only Architecture Reviewer (`.github/agents/kmp-reviewer.agent.md`)
```markdown
---
name: kmp-reviewer
description: Reviews Kotlin Multiplatform PRs for Clean Architecture purity and platform portability without modifying code.
tools:
  - read
  - search
---
Inspect changes in `shared/` and `composeApp/`.
Verify that `domain/` remains pure Kotlin with zero Android, Ktor, or Compose imports.
Do not edit files or execute shell commands. Report findings by file path and line number.
```

### B. Scoped KMP Implementer (`.github/agents/kmp-implementer.agent.md`)
```markdown
---
name: kmp-implementer
description: Implements features and unit tests in the KMP scaffold and runs Gradle verification.
tools:
  - read
  - search
  - edit
  - execute
---
Follow `.github/copilot-instructions.md` and `.github/instructions/kmp-architecture.instructions.md`.
Implement changes on a feature branch and verify using `./gradlew :shared:assemble :shared:jvmTest`.
```

---

## 4. Cloud-Agent Environment Setup (`.github/workflows/copilot-setup-steps.yml`)

When tasks are delegated to GitHub Copilot cloud agent (`/delegate` or `&`), execution occurs in an ephemeral GitHub-hosted runner. Without explicit setup steps, Gradle and toolchain bootstrapping fail or waste quota.

### Requirements
- **File location:** `.github/workflows/copilot-setup-steps.yml`
- **Default branch:** Must exist on the default branch (`master`/`main`) to execute on cloud tasks.
- **Mandatory job identifier:** Must be named `copilot-setup-steps`.

```yaml
name: Copilot Setup Steps

on:
  workflow_dispatch:

jobs:
  copilot-setup-steps:
    runs-on: ubuntu-latest
    permissions:
      contents: read
    steps:
      - uses: actions/checkout@v4

      - name: Set up JDK 21
        uses: actions/setup-java@v4
        with:
          distribution: temurin
          java-version: "21"

      - name: Setup Gradle
        uses: gradle/actions/setup-gradle@v3
        with:
          gradle-version: wrapper

      - name: Warm Gradle dependencies
        run: ./gradlew :shared:dependencies --no-daemon
```

---

## 5. Skills & Reusable Prompts

### A. Troubleshooting Skill (`.github/skills/kmp-gradle-debugging/SKILL.md`)
```markdown
---
name: kmp-gradle-debugging
description: Diagnoses Kotlin Multiplatform compilation, dependency, and expect/actual mismatches.
---
1. Run `./gradlew :shared:jvmTest --stacktrace` to isolate the failing source set.
2. Check `gradle/libs.versions.toml` for Ktor, Koin, or Kotlin version conflicts.
3. Verify every `expect` declaration in `commonMain` has matching `actual` implementations in `androidMain`, `jvmMain`, and `wasmJsMain`.
```

### B. UseCase Prompt Template (`.github/prompts/new-usecase.prompt.md`)
```markdown
---
name: new-usecase
description: Scaffolds a new clean architecture UseCase in domain/, binds it in Koin di/, and adds unit tests.
---
Create a new UseCase for the specified feature:
1. Define a pure Kotlin interface/class under `shared/src/commonMain/kotlin/com/example/shared/domain/usecase/`.
2. Avoid any framework, Android, Ktor, or UI imports in the domain layer.
3. Register the UseCase factory/single in `shared/src/commonMain/kotlin/com/example/shared/di/AppModule.kt`.
4. Add unit test coverage in `shared/src/commonTest/` or `shared/src/jvmTest/`.
5. Run `./gradlew :shared:jvmTest` to verify.
```

---

## 6. Model Context Protocol (MCP) Configuration

### Configuration Syntax Nuance
- In **custom agent YAML**, use `mcp-servers` (kebab-case).
- In **standalone JSON** (`.github/mcp.json`, `.vscode/mcp.json`), use `mcpServers` (camelCase).

### Transport Types
- `local` / `stdio`: Local subprocess running commands (e.g., `npx`, `python`). Uses `command` and `args`.
- `http`: Modern remote Streamable HTTP server. Uses `url` and `headers`.
- `sse`: Legacy Server-Sent Events remote server. Uses `url` and `headers`.

### Cloud-Agent Secret Rule
All secrets referenced in cloud-agent MCP configurations must be prefixed with `COPILOT_MCP_` (e.g., `$COPILOT_MCP_GITHUB_TOKEN`).

### Project Configuration (`.github/mcp.json`)
```json
{
  "mcpServers": {
    "github": {
      "type": "local",
      "command": "npx",
      "args": ["-y", "@modelcontextprotocol/server-github"],
      "tools": ["*"],
      "env": {
        "GITHUB_PERSONAL_ACCESS_TOKEN": "$COPILOT_MCP_GITHUB_TOKEN"
      }
    }
  }
}
```

---

## 7. Non-Interactive CLI Automation & CI Traps

When executing Copilot CLI in headless CI workflows:
- Always supply `--no-ask-user` to prevent terminal hangs.
- Explicitly scope allowed tools using `--allow-tool`.
- Authenticate via `COPILOT_GITHUB_TOKEN`.

```bash
copilot -p "Audit dependencies in build.gradle.kts" \
  --allow-tool="read,search" \
  --no-ask-user
```

---

## 8. Complete Domain 2 File Layout

```text
<repository-root>/
├── .github/
│   ├── agents/
│   │   ├── kmp-reviewer.agent.md               # Read-only architecture reviewer [read, search]
│   │   └── kmp-implementer.agent.md            # Bounded implementer [read, search, edit, execute]
│   ├── workflows/
│   │   ├── ci.yml                              # Concurrency-controlled CI status gate [Domain 1]
│   │   └── copilot-setup-steps.yml             # Cloud-agent environment setup [Domain 2]
│   ├── skills/
│   │   └── kmp-gradle-debugging/
│   │       └── SKILL.md                        # Reusable diagnostic skill [Domain 2]
│   ├── prompts/
│   │   └── new-usecase.prompt.md               # Reusable UseCase scaffolding prompt [Domain 2]
│   ├── mcp.json                                # Repo MCP configuration [Domain 2]
│   ├── copilot-instructions.md                 # Universal repo guardrails [Domain 1]
│   └── pull_request_template.md                # PR gate template [Domain 1]
├── docs/
│   ├── plans/
│   │   └── kmp-scaffold-domain2-plan.md        # Durable Domain 2 plan artifact
│   └── protect-master-ruleset.json             # Branch ruleset schema [Domain 1]
├── .vscode/
│   └── mcp.json                                # Local editor MCP configuration
├── domain2_scaffold_kmp_steps.scp              # Executable verification and push guide
└── SDLC.scp                                    # Updated repo architecture map
```

---

## 9. How to Send Up Domain 2 to GitHub (Step-by-Step)

### Step 1: Create Branch & Add Artifacts
```bash
git checkout -b feat/domain-2-agent-tools-and-environment
git add .github/ docs/ .vscode/
git commit -m "chore: scaffold GH-600 domain 2 agent tools, skills, and cloud environment"
```

### Step 2: Push Feature Branch
```bash
git push -u origin feat/domain-2-agent-tools-and-environment
```

### Step 3: Open Pull Request
Navigate to:
```text
https://github.com/defconradio/<repo>/compare/master...feat/domain-2-agent-tools-and-environment
```
Title:
```text
chore(sdlc): scaffold GH-600 Domain 2 agent tools, skills, and cloud environment
```
Fill out the PR description with the template, verify validation items, and merge upon approval.
