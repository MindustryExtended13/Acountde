package acountde.ui.dialog;

import acountde.Acountde;
import acountde.content.ACRegistry;
import acountde.ui.UIUtil;
import arc.Core;
import arc.graphics.Color;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.layout.Scl;
import arc.struct.Seq;
import arc.util.Time;
import mindustry.ctype.UnlockableContent;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;

import static mindustry.Vars.*;

public class AcountdeDialog extends BuildableDialog {
    public AcountdeDialog() {
        super(Acountde.MOD_NAME);
    }

    @Override
    public void build() {
        buttons.clearChildren();
        cont.clearChildren();

        var mod = Acountde.instance;
        cont.pane(desc -> {
            desc.center();
            desc.defaults().padTop(10).left();

            desc.add("@editor.name").padRight(10).color(Color.gray).padTop(0);
            desc.row();
            desc.add(mod.meta.displayName()).growX().wrap().padTop(2);
            desc.row();
            if(mod.meta.author != null) {
                desc.add("@editor.author").padRight(10).color(Color.gray);
                desc.row();
                desc.add(mod.meta.author).growX().wrap().padTop(2);
                desc.row();
            }
            if(mod.meta.description != null) {
                desc.add("@editor.description").padRight(10).color(Color.gray).top();
                desc.row();
                desc.add(mod.meta.description).growX().wrap().padTop(2);
                desc.row();
            }
        }).width(400f);

        Seq<UnlockableContent> all = ACRegistry.displayAbleContent();
        cont.row();
        UIUtil.labelButton(cont, "@mods.viewcontent", () -> {
            BaseDialog d = new BaseDialog(mod.meta.displayName());
            d.cont.pane(cs -> {
                int i = 0;
                for(UnlockableContent c : all){
                    cs.button(new TextureRegionDrawable(c.uiIcon), Styles.flati, iconMed, () -> {
                        try {
                            ui.content.show(c);
                        } catch(Throwable throwable) {
                            Acountde.fail(throwable);
                        }
                    }).size(50f).with(im -> {
                        var click = im.getClickListener();
                        im.update(() -> {
                            im.getImage().color.lerp(
                                    !click.isOver() ? Color.lightGray : Color.white, 0.4f * Time.delta
                            );
                        });
                    }).tooltip(c.localizedName);

                    if(++i % (int)Math.min(Core.graphics.getWidth() / Scl.scl(110), 14) == 0) cs.row();
                }
            }).grow();
            d.addCloseButton();
            d.show();
        }).pad(4).padTop(32).row();

        if(!mobile) {
            UIUtil.labelButton(cont, "@mods.openfolder", () -> {
                Core.app.openFolder(mod.file.absolutePath());
            }).pad(4).row();
        }

        UIUtil.labelButton(cont, "@mods.github.open", () -> {
            Core.app.openURI(Acountde.MOD_GITHUB);
        }).pad(4).row();

        UIUtil.labelButton(cont, "@mods.browser.reinstall", () -> {
            ui.mods.githubImportMod(mod.getRepo(), mod.isJava(), null);
        }).pad(4).row();

        UIUtil.labelButton(cont, "@back", this::hide).pad(4);
    }
}