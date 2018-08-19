package isel.leic.daw.dawProject.model.outputModel.account

import com.google.code.siren4j.annotations.*
import com.google.code.siren4j.component.impl.ActionImpl
import isel.leic.daw.dawProject.model.outputModel.checklist.collection.CollectionChecklistSirenHTO
import isel.leic.daw.dawProject.model.outputModel.template.collection.CollectionTemplateSirenHTO

@Siren4JEntity(
        name = "account",
        suppressClassProperty = true,
        uri = "/accounts/{username}",
        actions = [
            Siren4JAction(
                    name = "update-Account", method = ActionImpl.Method.PUT,
                    href = "/accounts/{username}",
                    type = "application/json"
            ),
            Siren4JAction(
                    name = "delete-Account", method = ActionImpl.Method.DELETE,
                    href = "/accounts/{username}",
                    type = "application/json"
            )
        ]
)
class AccountSirenHTO (
        @Siren4JSubEntity(rel = ["checklists"], uri = "/checklists", embeddedLink = true)
        var checklists: CollectionChecklistSirenHTO = CollectionChecklistSirenHTO(),

        @Siren4JSubEntity(rel = ["templates"], uri = "/templates", embeddedLink = true)
        var templates: CollectionTemplateSirenHTO = CollectionTemplateSirenHTO(),

        val username: String = "",

        val sub: String = "",

        val name: String = "",

        val email: String = ""
)