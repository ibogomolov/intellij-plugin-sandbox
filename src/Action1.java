import com.intellij.codeInsight.actions.SimpleCodeInsightAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;

/**
 * Created by ibogomolov on 09.04.14.
 */
public class Action1 extends SimpleCodeInsightAction {
    @Override
    public void invoke(@NotNull Project project, @NotNull Editor editor, @NotNull PsiFile file) {
        final int offset = editor.getCaretModel().getOffset();
        final PsiElement element = file.findElementAt(offset);

        PsiCall methodCall = PsiTreeUtil.getParentOfType(element, PsiCall.class);
        PsiMethod method = methodCall.resolveMethod();
        PsiCodeBlock body = method.getBody();

        PsiElement methodCallParent = methodCall.getParent();
        PsiElement statement = methodCall.addAfter(body, methodCallParent);
        statement = CodeStyleManager.getInstance(project).reformat(statement);
    }

    @Override
    public void update(AnActionEvent e) {
        e.getPresentation().setEnabled(getPsiMethodCallFromContext(e) != null);
    }

    public boolean startInWriteAction() {
        return true;
    }

    private PsiMethodCallExpression getPsiMethodCallFromContext(AnActionEvent e) {
        PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);
        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        if (psiFile == null || editor == null) {
            return null;
        }
        int offset = editor.getCaretModel().getOffset();
        PsiElement elementAt = psiFile.findElementAt(offset);
        return PsiTreeUtil.getParentOfType(elementAt, PsiMethodCallExpression.class);
    }



//    public void actionPerformed(AnActionEvent e) {
//        Editor editor = e.getData(PlatformDataKeys.EDITOR);
//        final int offset = editor.getCaretModel().getOffset();
//        PsiFile file = e.getData(LangDataKeys.PSI_FILE);
//        final PsiElement element = file.findElementAt(offset);
////        PsiElement element = e.getData(CommonDataKeys.PSI_ELEMENT);
//        PsiMethodCallExpression psiMethodCall = getPsiMethodCallFromContext(e);
//
//        // TODO get method body
//
//        Project project = psiMethodCall.getProject();
//        final PsiElementFactory factory = JavaPsiFacade.getInstance(project).getElementFactory();
//
////        PsiElement statement = factory.createStatementFromText("// my comment here", null);
//        PsiElement statement = factory.createCodeBlockFromText("{ // my comment here }", null);
//        statement = CodeStyleManager.getInstance(project).reformat(statement);
//        statement = psiMethodCall.replace(statement);
//    }

//    private PsiClass getPsiClassFromContext(AnActionEvent e) {
//        PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);
//        Editor editor = e.getData(PlatformDataKeys.EDITOR);
//        if (psiFile == null || editor == null) {
//            return null;
//        }
//        int offset = editor.getCaretModel().getOffset();
//        PsiElement elementAt = psiFile.findElementAt(offset);
//        PsiClass psiClass = PsiTreeUtil.getParentOfType(elementAt, PsiClass.class);
//        return psiClass;
//    }
}
