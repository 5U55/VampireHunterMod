package com.efs.vampirehunters.bossmob;

import com.efs.vampirehunters.VampireHuntersClient;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class VampireEntityRenderer extends MobEntityRenderer<VampireEntity, VampireEntityModel> {
 
    public VampireEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new VampireEntityModel(context.getPart(VampireHuntersClient.MODEL_VAMPIRE_LAYER)), 0.5f);
    }
 
    @Override
    public Identifier getTexture(VampireEntity entity) {
        return new Identifier("vampirehunters","textures/entity/vampire.png");
    }


}