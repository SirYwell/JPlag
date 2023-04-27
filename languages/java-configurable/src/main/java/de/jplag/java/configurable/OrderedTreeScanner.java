package de.jplag.java.configurable;

import com.sun.source.tree.AnnotatedTypeTree;
import com.sun.source.tree.AnnotationTree;
import com.sun.source.tree.ArrayAccessTree;
import com.sun.source.tree.ArrayTypeTree;
import com.sun.source.tree.AssertTree;
import com.sun.source.tree.AssignmentTree;
import com.sun.source.tree.BinaryTree;
import com.sun.source.tree.BindingPatternTree;
import com.sun.source.tree.BlockTree;
import com.sun.source.tree.BreakTree;
import com.sun.source.tree.CaseTree;
import com.sun.source.tree.CatchTree;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.CompoundAssignmentTree;
import com.sun.source.tree.ConditionalExpressionTree;
import com.sun.source.tree.ContinueTree;
import com.sun.source.tree.DefaultCaseLabelTree;
import com.sun.source.tree.DoWhileLoopTree;
import com.sun.source.tree.EmptyStatementTree;
import com.sun.source.tree.EnhancedForLoopTree;
import com.sun.source.tree.ErroneousTree;
import com.sun.source.tree.ExportsTree;
import com.sun.source.tree.ExpressionStatementTree;
import com.sun.source.tree.ForLoopTree;
import com.sun.source.tree.GuardedPatternTree;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.IfTree;
import com.sun.source.tree.ImportTree;
import com.sun.source.tree.InstanceOfTree;
import com.sun.source.tree.IntersectionTypeTree;
import com.sun.source.tree.LabeledStatementTree;
import com.sun.source.tree.LambdaExpressionTree;
import com.sun.source.tree.LiteralTree;
import com.sun.source.tree.MemberReferenceTree;
import com.sun.source.tree.MemberSelectTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.ModifiersTree;
import com.sun.source.tree.ModuleTree;
import com.sun.source.tree.NewArrayTree;
import com.sun.source.tree.NewClassTree;
import com.sun.source.tree.OpensTree;
import com.sun.source.tree.PackageTree;
import com.sun.source.tree.ParameterizedTypeTree;
import com.sun.source.tree.ParenthesizedPatternTree;
import com.sun.source.tree.ParenthesizedTree;
import com.sun.source.tree.PrimitiveTypeTree;
import com.sun.source.tree.ProvidesTree;
import com.sun.source.tree.RequiresTree;
import com.sun.source.tree.ReturnTree;
import com.sun.source.tree.SwitchExpressionTree;
import com.sun.source.tree.SwitchTree;
import com.sun.source.tree.SynchronizedTree;
import com.sun.source.tree.ThrowTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.TreeVisitor;
import com.sun.source.tree.TryTree;
import com.sun.source.tree.TypeCastTree;
import com.sun.source.tree.TypeParameterTree;
import com.sun.source.tree.UnaryTree;
import com.sun.source.tree.UnionTypeTree;
import com.sun.source.tree.UsesTree;
import com.sun.source.tree.VariableTree;
import com.sun.source.tree.WhileLoopTree;
import com.sun.source.tree.WildcardTree;
import com.sun.source.tree.YieldTree;

/**
 * Base class to visit an AST in the order of the appearance of the elements.
 */
public abstract class OrderedTreeScanner implements TreeVisitor<Void, Void> {

    private void scan(Tree tree) {
        if (tree == null) {
            return;
        }
        pre(tree);
        tree.accept(this, null);
        post(tree);
    }

    private void scanAll(Iterable<? extends Tree> iterable) {
        if (iterable == null) {
            return;
        }
        enterAll(iterable);
        for (Tree tree : iterable) {
            scan(tree);
        }
        exitAll(iterable);
    }

    protected abstract void pre(Tree tree);

    protected abstract void middle(Tree tree);

    protected abstract void post(Tree tree);

    protected abstract void enterAll(Iterable<? extends Tree> iterable);

    protected abstract void exitAll(Iterable<? extends Tree> iterable);

    @Override
    public Void visitAnnotatedType(AnnotatedTypeTree node, Void unused) {
        scanAll(node.getAnnotations());
        scan(node.getUnderlyingType());
        return null;
    }

    @Override
    public Void visitAnnotation(AnnotationTree node, Void unused) {
        scan(node.getAnnotationType());
        scanAll(node.getArguments());
        return null;
    }

    @Override
    public Void visitMethodInvocation(MethodInvocationTree node, Void unused) {
        scan(node.getMethodSelect());
        scanAll(node.getTypeArguments());
        scanAll(node.getArguments());
        return null;
    }

    @Override
    public Void visitAssert(AssertTree node, Void unused) {
        scan(node.getCondition());
        scan(node.getDetail());
        return null;
    }

    @Override
    public Void visitAssignment(AssignmentTree node, Void unused) {
        scan(node.getVariable());
        middle(node);
        scan(node.getExpression());
        return null;
    }

    @Override
    public Void visitCompoundAssignment(CompoundAssignmentTree node, Void unused) {
        scan(node.getVariable());
        middle(node);
        scan(node.getExpression());
        return null;
    }

    @Override
    public Void visitBinary(BinaryTree node, Void unused) {
        scan(node.getLeftOperand());
        middle(node);
        scan(node.getRightOperand());
        return null;
    }

    @Override
    public Void visitBlock(BlockTree node, Void unused) {
        scanAll(node.getStatements());
        return null;
    }

    @Override
    public Void visitBreak(BreakTree node, Void unused) {
        return null;
    }

    @Override
    public Void visitCase(CaseTree node, Void unused) {
        // scanAll(node.getExpressions()); included in Labels
        scanAll(node.getLabels());
        if (node.getCaseKind() == CaseTree.CaseKind.RULE) {
            scan(node.getBody());
        } else {
            scanAll(node.getStatements());
        }
        return null;
    }

    @Override
    public Void visitCatch(CatchTree node, Void unused) {
        scan(node.getParameter());
        scan(node.getBlock());
        return null;
    }

    @Override
    public Void visitClass(ClassTree node, Void unused) {
        scan(node.getModifiers());
        middle(node);
        scanAll(node.getTypeParameters());
        scan(node.getExtendsClause());
        scanAll(node.getImplementsClause());
        scanAll(node.getPermitsClause());
        scanAll(node.getMembers());
        return null;
    }

    @Override
    public Void visitConditionalExpression(ConditionalExpressionTree node, Void unused) {
        scan(node.getCondition());
        scan(node.getTrueExpression());
        scan(node.getFalseExpression());
        return null;
    }

    @Override
    public Void visitContinue(ContinueTree node, Void unused) {
        return null;
    }

    @Override
    public Void visitDoWhileLoop(DoWhileLoopTree node, Void unused) {
        scan(node.getStatement());
        scan(node.getCondition());
        return null;
    }

    @Override
    public Void visitErroneous(ErroneousTree node, Void unused) {
        return null;
    }

    @Override
    public Void visitExpressionStatement(ExpressionStatementTree node, Void unused) {
        scan(node.getExpression());
        return null;
    }

    @Override
    public Void visitEnhancedForLoop(EnhancedForLoopTree node, Void unused) {
        scan(node.getVariable());
        scan(node.getExpression());
        scan(node.getStatement());
        return null;
    }

    @Override
    public Void visitForLoop(ForLoopTree node, Void unused) {
        scanAll(node.getInitializer());
        scan(node.getCondition());
        scanAll(node.getUpdate());
        scan(node.getStatement());
        return null;
    }

    @Override
    public Void visitIdentifier(IdentifierTree node, Void unused) {
        return null;
    }

    @Override
    public Void visitIf(IfTree node, Void unused) {
        scan(node.getCondition());
        scan(node.getThenStatement());
        // special case: visit IfTree as middle for else block
        if (node.getElseStatement() != null) {
            middle(node);
        }
        scan(node.getElseStatement());
        return null;
    }

    @Override
    public Void visitImport(ImportTree node, Void unused) {
        scan(node.getQualifiedIdentifier());
        return null;
    }

    @Override
    public Void visitArrayAccess(ArrayAccessTree node, Void unused) {
        scan(node.getExpression());
        scan(node.getIndex());
        return null;
    }

    @Override
    public Void visitLabeledStatement(LabeledStatementTree node, Void unused) {
        scan(node.getStatement());
        return null;
    }

    @Override
    public Void visitLiteral(LiteralTree node, Void unused) {
        return null;
    }

    @Override
    public Void visitBindingPattern(BindingPatternTree node, Void unused) {
        scan(node.getVariable());
        return null;
    }

    @Override
    public Void visitDefaultCaseLabel(DefaultCaseLabelTree node, Void unused) {
        return null;
    }

    @Override
    public Void visitMethod(MethodTree node, Void unused) {
        scan(node.getModifiers());
        scanAll(node.getTypeParameters());
        scan(node.getReturnType());
        middle(node);
        scan(node.getReceiverParameter());
        scanAll(node.getParameters());
        scanAll(node.getThrows());
        scan(node.getDefaultValue());
        scan(node.getBody());
        return null;
    }

    @Override
    public Void visitModifiers(ModifiersTree node, Void unused) {
        scanAll(node.getAnnotations());
        // flags
        return null;
    }

    @Override
    public Void visitNewArray(NewArrayTree node, Void unused) {
        scanAll(node.getAnnotations());
        scan(node.getType());
        // TODO dim annotations
        scanAll(node.getDimensions());
        scanAll(node.getInitializers());
        return null;
    }

    @Override
    public Void visitGuardedPattern(GuardedPatternTree node, Void unused) {
        scan(node.getPattern());
        scan(node.getExpression());
        return null;
    }

    @Override
    public Void visitParenthesizedPattern(ParenthesizedPatternTree node, Void unused) {
        scan(node.getPattern());
        return null;
    }

    @Override
    public Void visitNewClass(NewClassTree node, Void unused) {
        scan(node.getEnclosingExpression());
        // new keyword
        scanAll(node.getTypeArguments());
        scan(node.getIdentifier());
        scanAll(node.getArguments());
        scan(node.getClassBody());
        return null;
    }

    @Override
    public Void visitLambdaExpression(LambdaExpressionTree node, Void unused) {
        scanAll(node.getParameters());
        scan(node.getBody());
        return null;
    }

    @Override
    public Void visitPackage(PackageTree node, Void unused) {
        scanAll(node.getAnnotations());
        scan(node.getPackageName());
        return null;
    }

    @Override
    public Void visitParenthesized(ParenthesizedTree node, Void unused) {
        scan(node.getExpression());
        return null;
    }

    @Override
    public Void visitReturn(ReturnTree node, Void unused) {
        scan(node.getExpression());
        return null;
    }

    @Override
    public Void visitMemberSelect(MemberSelectTree node, Void unused) {
        scan(node.getExpression());
        return null;
    }

    @Override
    public Void visitMemberReference(MemberReferenceTree node, Void unused) {
        scan(node.getQualifierExpression());
        scanAll(node.getTypeArguments());
        return null;
    }

    @Override
    public Void visitEmptyStatement(EmptyStatementTree node, Void unused) {
        return null;
    }

    @Override
    public Void visitSwitch(SwitchTree node, Void unused) {
        scan(node.getExpression());
        scanAll(node.getCases());
        return null;
    }

    @Override
    public Void visitSwitchExpression(SwitchExpressionTree node, Void unused) {
        scan(node.getExpression());
        scanAll(node.getCases());
        return null;
    }

    @Override
    public Void visitSynchronized(SynchronizedTree node, Void unused) {
        scan(node.getExpression());
        scan(node.getBlock());
        return null;
    }

    @Override
    public Void visitThrow(ThrowTree node, Void unused) {
        scan(node.getExpression());
        return null;
    }

    @Override
    public Void visitCompilationUnit(CompilationUnitTree node, Void unused) {
        if (node.getModule() != null) {
            scan(node.getModule());
        }
        if (node.getPackage() != null) {
            scanAll(node.getPackageAnnotations());
            scan(node.getPackage());
        }
        scanAll(node.getImports());
        scanAll(node.getTypeDecls());
        return null;
    }

    @Override
    public Void visitTry(TryTree node, Void unused) {
        scanAll(node.getResources());
        scan(node.getBlock());
        scanAll(node.getCatches());
        // special case: visit TryTree as middle for finally block
        if (node.getFinallyBlock() != null) {
            middle(node);
        }
        scan(node.getFinallyBlock());
        return null;
    }

    @Override
    public Void visitParameterizedType(ParameterizedTypeTree node, Void unused) {
        scan(node.getType());
        scanAll(node.getTypeArguments());
        return null;
    }

    @Override
    public Void visitUnionType(UnionTypeTree node, Void unused) {
        scanAll(node.getTypeAlternatives());
        return null;
    }

    @Override
    public Void visitIntersectionType(IntersectionTypeTree node, Void unused) {
        scanAll(node.getBounds());
        return null;
    }

    @Override
    public Void visitArrayType(ArrayTypeTree node, Void unused) {
        scan(node.getType());
        return null;
    }

    @Override
    public Void visitTypeCast(TypeCastTree node, Void unused) {
        scan(node.getType());
        scan(node.getExpression());
        return null;
    }

    @Override
    public Void visitPrimitiveType(PrimitiveTypeTree node, Void unused) {
        return null;
    }

    @Override
    public Void visitTypeParameter(TypeParameterTree node, Void unused) {
        scanAll(node.getAnnotations());
        middle(node);
        scanAll(node.getBounds());
        return null;
    }

    @Override
    public Void visitInstanceOf(InstanceOfTree node, Void unused) {
        scan(node.getExpression());
        scan(node.getType());
        scan(node.getPattern());
        return null;
    }

    @Override
    public Void visitUnary(UnaryTree node, Void unused) {
        scan(node.getExpression());
        return null;
    }

    @Override
    public Void visitVariable(VariableTree node, Void unused) {
        scan(node.getModifiers());
        scan(node.getType());
        scan(node.getNameExpression());
        scan(node.getInitializer());
        return null;
    }

    @Override
    public Void visitWhileLoop(WhileLoopTree node, Void unused) {
        scan(node.getCondition());
        scan(node.getStatement());
        return null;
    }

    @Override
    public Void visitWildcard(WildcardTree node, Void unused) {
        scan(node.getBound());
        return null;
    }

    @Override
    public Void visitModule(ModuleTree node, Void unused) {
        scanAll(node.getAnnotations());
        scan(node.getName());
        scanAll(node.getDirectives());
        return null;
    }

    @Override
    public Void visitExports(ExportsTree node, Void unused) {
        return null;
    }

    @Override
    public Void visitOpens(OpensTree node, Void unused) {
        return null;
    }

    @Override
    public Void visitProvides(ProvidesTree node, Void unused) {
        return null;
    }

    @Override
    public Void visitRequires(RequiresTree node, Void unused) {
        return null;
    }

    @Override
    public Void visitUses(UsesTree node, Void unused) {
        return null;
    }

    @Override
    public Void visitOther(Tree node, Void unused) {
        return null;
    }

    @Override
    public Void visitYield(YieldTree node, Void unused) {
        scan(node.getValue());
        return null;
    }
}
