# Agent Plan: KMP-scaffold Domain 1 Agent Architecture & SDLC Setup

## Goal
Establish GitHub-native Agent Architecture and SDLC processes for the Kotlin Multiplatform (KMP) scaffold following the GH-600 Domain 1 standard.

## Scope & Autonomy Level
- **Autonomy Level:** Medium (create configuration files, update CI workflow with concurrency controls, prepare ruleset definitions, run local Gradle builds).
- **Inputs:** Study guide requirements from GH-600 Domain 1, existing Gradle/KMP setup in `KMP-scaffold`.
- **Outputs:** Repository instructions, path-specific instructions, PR template, CI workflow concurrency/permissions hardening, branch ruleset JSON, and executable setup guide.

## Steps
1. **Branch Isolation:**
   - Create and switch to feature branch `feat/domain-1-agent-architecture-sdlc`.
   - Never push directly to `master`.
2. **Instruction Architecture:**
   - Author `.github/copilot-instructions.md` with KMP stack rules, Clean Architecture layers, secret protections, and test commands.
   - Author `.github/instructions/kmp-architecture.instructions.md` targeting `shared/**` and `composeApp/**` with domain isolation rules.
3. **PR Accountability Template:**
   - Author `.github/pull_request_template.md` with explicit sections for Summary, Risk Notes (multiplatform targets, API breaks, build changes), and Validation Evidence.
4. **CI Workflow Gating & Concurrency:**
   - Update `.github/workflows/ci.yml` with least-privilege permissions (`contents: read`) and branch concurrency (`cancel-in-progress: true`).
5. **Branch Ruleset Policy:**
   - Create `docs/protect-master-ruleset.json` enforcing required PR approval, last push approval, non-fast-forward, deletion protection, and required status check matching CI.
6. **Architecture Map & Setup Automation:**
   - Create `SDLC.scp` mapping out the agent-aware architecture and SDLC workflow.
   - Create `domain1_scaffold_kmp_steps.scp` providing instructions to configure the remote and apply the ruleset on GitHub.

## Validation Criteria
- `./gradlew :shared:assemble :shared:jvmTest` succeeds locally.
- Git working tree contains only relevant Domain 1 configuration files.
- `.github/workflows/ci.yml` contains valid YAML syntax with concurrency and permissions defined.
- Ruleset JSON conforms to GitHub branch ruleset schema.
