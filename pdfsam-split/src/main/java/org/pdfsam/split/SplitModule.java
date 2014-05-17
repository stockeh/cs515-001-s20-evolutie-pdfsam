/* 
 * This file is part of the PDF Split And Merge source code
 * Created on 07/apr/2014
 * Copyright 2013-2014 by Andrea Vacondio (andrea.vacondio@gmail.com).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as 
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.pdfsam.split;

import static org.pdfsam.module.ModuleDescriptorBuilder.builder;

import java.io.IOException;
import java.util.function.Consumer;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import org.pdfsam.context.DefaultI18nContext;
import org.pdfsam.module.ModuleCategory;
import org.pdfsam.module.ModuleDescriptor;
import org.pdfsam.module.ModulePriority;
import org.pdfsam.module.PdfsamModule;
import org.pdfsam.ui.io.BrowsableDirectoryField;
import org.pdfsam.ui.io.PdfDestinationPane;
import org.pdfsam.ui.module.BaseTaskExecutionModule;
import org.pdfsam.ui.selection.single.SingleSelectionPane;
import org.pdfsam.ui.support.Views;
import org.sejda.model.parameter.AbstractSplitByPageParameters;
import org.sejda.model.parameter.SplitByPagesParameters;
import org.sejda.model.parameter.base.TaskParameters;
import org.springframework.core.io.ClassPathResource;

/**
 * Merge module to let the user merge together multiple pdf documents
 * 
 * @author Andrea Vacondio
 *
 */
@PdfsamModule
public class SplitModule extends BaseTaskExecutionModule {

    private static final String SPLIT_MODULE_ID = "split";

    private SingleSelectionPane<AbstractSplitByPageParameters> selectionPane;
    private BrowsableDirectoryField destinationDirectoryField = new BrowsableDirectoryField(false);
    private PdfDestinationPane destinationPane;
    private ModuleDescriptor descriptor = builder().category(ModuleCategory.SPLIT)
            .name(DefaultI18nContext.getInstance().i18n("Split"))
            .description(DefaultI18nContext.getInstance().i18n("Split a pdf document at given page numbers"))
            .priority(ModulePriority.HIGH.getPriority()).supportURL("http://www.pdfsam.org/split").build();

    public SplitModule() {
        this.selectionPane = new SingleSelectionPane<>(id());
        this.destinationPane = new PdfDestinationPane(destinationDirectoryField, id());
    }

    @Override
    public ModuleDescriptor descriptor() {
        return descriptor;
    }

    @Override
    protected TaskParameters buildParameters(Consumer<String> onError) {
        SplitByPagesParameters params = new SplitByPagesParameters();
        selectionPane.apply(params, onError);
        destinationPane.apply(params, onError);
        return params;
    }

    @Override
    protected Pane getInnerPanel() {
        VBox pane = new VBox(5);
        pane.setAlignment(Pos.TOP_CENTER);
        VBox.setVgrow(selectionPane, Priority.ALWAYS);

        pane.getChildren().addAll(selectionPane,
                Views.titledPane(DefaultI18nContext.getInstance().i18n("Destination directory"), destinationPane));
        return pane;
    }

    @Override
    public String id() {
        return SPLIT_MODULE_ID;
    }

    public Node graphic() {
        try {
            return (Group) FXMLLoader.load(new ClassPathResource("/fxml/TestModule3.fxml").getURL());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}