package isel.leic.daw.dawProject.model.outputModel.checklist.collection

import com.google.code.siren4j.annotations.Siren4JEntity
import com.google.code.siren4j.resource.CollectionResource
import isel.leic.daw.dawProject.model.outputModel.checklist.single.ChecklistSirenHTO

@Siren4JEntity(name = "checklist-collection", suppressClassProperty = true)
class CollectionChecklistSirenHTO(
        val checklists: Collection<ChecklistSirenHTO> = CollectionResource()
) : CollectionResource<ChecklistSirenHTO>() {
    init {
        addAll(checklists)
    }
}