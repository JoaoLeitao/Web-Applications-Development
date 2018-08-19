package isel.leic.daw.dawProject.model.mapper

import isel.leic.daw.dawProject.model.inputModel.TemplateDTO
import isel.leic.daw.dawProject.model.entity.Template

class TemplateMapper {
    companion object Mapper {
        fun map(t: Template) =
                TemplateDTO(t.templateName)

        fun mapAll(ts : List<Template>) =
                ts.map { t -> map(t) }
    }
}