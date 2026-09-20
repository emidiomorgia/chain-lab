package com.chainlab.backend.controller;

import com.chainlab.backend.controller.dto.BlockCreatedResponse;
import com.chainlab.backend.service.BlockService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

@Path("/blocks")
public class BlockController {

    @Inject
    BlockService blockService;

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadFile(@RestForm("file") FileUpload file) {
        if (file == null || file.size() == 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Il file è obbligatorio e non può essere vuoto")
                    .build();
        }

        long index = blockService.createBlock(file.fileName(), file.size(), file.uploadedFile());
        return Response.status(Response.Status.CREATED)
                .entity(new BlockCreatedResponse(index))
                .build();
    }
}
