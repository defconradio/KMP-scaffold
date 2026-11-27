# Agent Plan: KMP-scaffold Domain 2 Agent Tools, Skills, and Cloud Environment Setup

## Goal
Implement GitHub-native custom agent profiles, cloud-agent setup workflow, reusable skills, prompt templates, and MCP configuration for the Kotlin Multiplatform (KMP) scaffold following the GH-600 Domain 2 specification.

## Scope & Autonomy Level
- **Autonomy Level:** Medium (scaffolding custom agent profiles, environment workflow, reusable prompts/skills, and MCP configuration).
- **Inputs:** Requirements from `/home/blackbox/KMP-scaffold/domain2.scp` and GH-600 Domain 2 ("Implement tool use and environment interaction").
- **Outputs:**
  - Read-only architecture reviewer agent (`.github/agents/kmp-reviewer.agent.md`)
  - Scoped KMP implementer agent (`.github/agents/kmp-implementer.agent.md`)
  - Cloud-agent environment setup workflow (`.github/workflows/copilot-setup-steps.yml`)
  - Gradle debugging skill (`.github/skills/kmp-gradle-debugging/SKILL.md`)
  - Use case scaffolding prompt template (`.github/prompts/new-usecase.prompt.md`)
  - Project MCP configuration (`.github/mcp.json`)
  - Execution and remote setup steps (`domain2_scaffold_kmp_steps.scp`)

## Steps
1. **Branch Isolation:**
   - Create and check out branch `feat/domain-2-agent-tools-and-environment`.
2. **Custom Agent Profiles:**
   - Author `kmp-reviewer.agent.md` with least-privilege tools `[read, search]`.
   - Author `kmp-implementer.agent.md` with bounded execution tools `[read, search, edit, execute]`.
3. **Cloud-Agent Environment Setup:**
   - Author `.github/workflows/copilot-setup-steps.yml` with required job name `copilot-setup-steps`.
   - Install JDK 21 (Temurin), configure Gradle wrapper caching, and warm Gradle dependencies (`./gradlew :shared:dependencies`).
4. **Reusable Skills & Prompts:**
   - Author `.github/skills/kmp-gradle-debugging/SKILL.md` for fast troubleshooting.
   - Author `.github/prompts/new-usecase.prompt.md` to enforce Clean Architecture use-case structure.
5. **MCP Configuration:**
   - Author `.github/mcp.json` using `mcpServers` format and standard `COPILOT_MCP_` secret naming.
6. **Validation & Synchronization:**
   - Verify workflow YAML and agent markdown frontmatter.
   - Mirror configuration to `/home/blackbox/apagar-org`.
   - Commit and push both repositories to GitHub.

## Validation Criteria
- Agent YAML frontmatter contains required `description` and valid tool arrays.
- Setup workflow contains job ID `copilot-setup-steps` with JDK 21 and Gradle wrapper setup.
- Working tree is clean and committed to feature branch.
