package org.gsonformat.intellij.process;

import com.intellij.psi.*;
import org.gsonformat.intellij.entity.ConvertLibrary;
import org.gsonformat.intellij.entity.ClassEntity;


/**
 * Created by dim on 16/11/7.
 */
public class ClassProcessor {

    private PsiElementFactory factory;
    private PsiClass cls;
    private Processor processor;

    public ClassProcessor(PsiElementFactory factory, PsiClass cls) {
        this.factory = factory;
        this.cls = cls;
        processor = Processor.getProcessor(ConvertLibrary.from());
    }

    public void generate(ClassEntity classEntity, IProcessor visitor) {
        if (processor != null) processor.process(classEntity, factory, cls, visitor);
    }
}
