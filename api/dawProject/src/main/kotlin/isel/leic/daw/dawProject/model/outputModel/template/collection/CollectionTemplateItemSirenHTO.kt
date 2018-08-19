package isel.leic.daw.dawProject.model.outputModel.template.collection

import com.google.code.siren4j.annotations.Siren4JEntity
import com.google.code.siren4j.resource.CollectionResource
import isel.leic.daw.dawProject.model.outputModel.template.single.TemplateItemSirenHTO

@Siren4JEntity(name = "template-items-collection", suppressClassProperty = true)
class CollectionTemplateItemSirenHTO(
        templateItems: Collection<TemplateItemSirenHTO> = CollectionResource()
) : CollectionResource<TemplateItemSirenHTO>() {
    init {
        addAll(templateItems)
    }
}