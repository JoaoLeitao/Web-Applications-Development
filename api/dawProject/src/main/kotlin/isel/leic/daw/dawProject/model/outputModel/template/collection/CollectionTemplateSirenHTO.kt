package isel.leic.daw.dawProject.model.outputModel.template.collection

import com.google.code.siren4j.annotations.Siren4JEntity
import com.google.code.siren4j.resource.CollectionResource
import isel.leic.daw.dawProject.model.outputModel.template.single.TemplateSirenHTO

@Siren4JEntity(name = "template-collection", suppressClassProperty = true)
class CollectionTemplateSirenHTO(
        templateItems: Collection<TemplateSirenHTO> = CollectionResource()
) : CollectionResource<TemplateSirenHTO>() {
    init {
        addAll(templateItems)
    }
}