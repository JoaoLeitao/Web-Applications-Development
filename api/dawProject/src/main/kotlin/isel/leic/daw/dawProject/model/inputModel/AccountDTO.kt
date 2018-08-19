package isel.leic.daw.dawProject.model.inputModel

import isel.leic.daw.dawProject.model.entity.Checklist
import isel.leic.daw.dawProject.model.entity.Template

data class AccountDTO(
        var sub: String = "",
        var checklists: MutableSet<Checklist>? = null,
        var templates: MutableSet<Template>? = null
)