# Possible Improvements

This document lists optional improvements that should not be treated as core requirements for the first version of Spock.

## Apple Calendar And Apple Reminders Integration

Spock may integrate with Apple Calendar and Apple Reminders in the future, but they should not become the source of truth.

Recommended approach:

```text
Spock Core reminders = source of truth
Apple Calendar / Apple Reminders = optional external sync targets
```

Possible implementation options:

- AppleScript for a simple local prototype
- macOS Shortcuts for user-visible automation
- EventKit through a small native macOS helper
- CalDAV for a more portable calendar-oriented integration

Rules:

- Spock Core stores reminders in PostgreSQL.
- Apple integrations store their external identifier in Spock.
- If Apple data disappears or changes, Spock should be able to reconcile it.
- Spock should keep working without Apple Calendar or Apple Reminders.

Initial use cases:

- Create an Apple Reminder from a Spock reminder.
- Create an Apple Calendar event for time-bound events.
- Update or cancel the external Apple item when the Spock reminder changes.

Deferred questions:

- Whether Apple should be read-only, write-only, or bidirectional.
- How to handle conflicts between Spock and Apple edits.
- Whether a native macOS helper is worth the maintenance cost.
- How this should work when the Mac mini becomes the always-on server.
