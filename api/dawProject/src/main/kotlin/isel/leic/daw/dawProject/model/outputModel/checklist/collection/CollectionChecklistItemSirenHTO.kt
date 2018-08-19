package isel.leic.daw.dawProject.model.outputModel.checklist.collection

import com.google.code.siren4j.annotations.Siren4JEntity
import com.google.code.siren4j.resource.CollectionResource
import isel.leic.daw.dawProject.model.outputModel.checklist.single.ChecklistItemSirenHTO

@Siren4JEntity(name = "checklist-items-collection", suppressClassProperty = true)
class CollectionChecklistItemSirenHTO(
        val checklistItems: Collection<ChecklistItemSirenHTO> = CollectionResource()
) : CollectionResource<ChecklistItemSirenHTO>() {
    init {
        addAll(checklistItems)
    }
}
