package de.jplag.java;

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
public abstract class OrderedTreeScanner implements TreeVisitor<Void, Role> {

    @Deprecated // all elements should have a Role
    private void scan(Tree tree) {
        tree.accept(this, null);
    }

    private void scan(Tree tree, Role role) {
        enter(tree, role);
        tree.accept(this, role);
        exit(tree, role);
    }

    private void scanIfPresent(Tree node, Role role) {
        if (node != null) {
            scan(node, role);
        }
    }

    private void scanAll(Iterable<? extends Tree> iterable, Role role) {
        enterAll(iterable, role);
        for (Tree tree : iterable) {
            scan(tree, role);
        }
        exitAll(iterable, role);
    }

    protected abstract void enter(Tree tree, Role role);

    protected abstract void exit(Tree tree, Role role);

    protected abstract void enterAll(Iterable<? extends Tree> iterable, Role role);

    protected abstract void exitAll(Iterable<? extends Tree> iterable, Role role);

    @Override
    public Void visitAnnotatedType(AnnotatedTypeTree node, Role role) {
        scanAll(node.getAnnotations(), Role.ANNOTATION);
        scan(node.getUnderlyingType(), Role.TYPE);
        return null;
    }

    @Override
    public Void visitAnnotation(AnnotationTree node, Role role) {
        scan(node.getAnnotationType(), Role.ANNOTATION);
        scanAll(node.getArguments(), Role.ANNOTATION_ARGUMENT);
        return null;
    }

    @Override
    public Void visitMethodInvocation(MethodInvocationTree node, Role role) {
        scan(node.getMethodSelect()); // TODO
        scanAll(node.getTypeArguments(), Role.TYPE_ARGUMENT);
        scanAll(node.getArguments(), Role.METHOD_ARGUMENT);
        return null;
    }

    @Override
    public Void visitAssert(AssertTree node, Role role) {
        scan(node.getCondition(), Role.CONDITION);
        scan(node.getDetail()); // TODO
        return null;
    }

    @Override
    public Void visitAssignment(AssignmentTree node, Role role) {
        scan(node.getVariable(), Role.VARIABLE);
        scan(node.getExpression(), Role.EXPRESSION);
        return null;
    }

    @Override
    public Void visitCompoundAssignment(CompoundAssignmentTree node, Role role) {
        scan(node.getVariable(), Role.VARIABLE);
        scan(node.getExpression(), Role.EXPRESSION);
        return null;
    }

    @Override
    public Void visitBinary(BinaryTree node, Role role) {
        // TODO
        scan(node.getLeftOperand());
        scan(node.getRightOperand());
        return null;
    }

    @Override
    public Void visitBlock(BlockTree node, Role role) {
        scanAll(node.getStatements(), Role.STATEMENT);
        return null;
    }

    @Override
    public Void visitBreak(BreakTree node, Role role) {
        return null;
    }

    @Override
    public Void visitCase(CaseTree node, Role role) {
        // TODO
        return null;
    }

    @Override
    public Void visitCatch(CatchTree node, Role role) {
        scan(node.getParameter());
        scan(node.getBlock());
        return null;
    }

    @Override
    public Void visitClass(ClassTree node, Role role) {
        scan(node.getModifiers());
        // name
        scanAll(node.getTypeParameters(), Role.TYPE_PARAMETER);
        scanIfPresent(node.getExtendsClause(), Role.TYPE_REFERENCE);
        scanAll(node.getImplementsClause(), Role.TYPE_REFERENCE);
        scanAll(node.getPermitsClause(), Role.TYPE_REFERENCE);
        scanAll(node.getMembers(), Role.MEMBER);
        return null;
    }

    @Override
    public Void visitConditionalExpression(ConditionalExpressionTree node, Role role) {
        scan(node.getCondition(), Role.CONDITION);
        scan(node.getTrueExpression());
        scan(node.getFalseExpression());
        return null;
    }

    @Override
    public Void visitContinue(ContinueTree node, Role role) {
        return null;
    }

    @Override
    public Void visitDoWhileLoop(DoWhileLoopTree node, Role role) {
        scan(node.getStatement(), Role.STATEMENT);
        scan(node.getCondition(), Role.CONDITION);
        return null;
    }

    @Override
    public Void visitErroneous(ErroneousTree node, Role role) {
        return null;
    }

    @Override
    public Void visitExpressionStatement(ExpressionStatementTree node, Role role) {
        scan(node.getExpression(), Role.EXPRESSION);
        return null;
    }

    @Override
    public Void visitEnhancedForLoop(EnhancedForLoopTree node, Role role) {
        scan(node.getVariable(), Role.VARIABLE);
        scan(node.getExpression(), Role.EXPRESSION);
        scan(node.getStatement(), Role.STATEMENT);
        return null;
    }

    @Override
    public Void visitForLoop(ForLoopTree node, Role role) {
        scanAll(node.getInitializer(), Role.EXPRESSION);
        scan(node.getCondition(), Role.EXPRESSION);
        scanAll(node.getUpdate(), Role.EXPRESSION);
        scan(node.getStatement(), Role.STATEMENT);
        return null;
    }

    @Override
    public Void visitIdentifier(IdentifierTree node, Role role) {
        return null;
    }

    @Override
    public Void visitIf(IfTree node, Role role) {
        scan(node.getCondition(), Role.CONDITION);
        scan(node.getThenStatement(), Role.THEN);
        scan(node.getElseStatement(), Role.ELSE);
        return null;
    }

    @Override
    public Void visitImport(ImportTree node, Role role) {
        // TODO
        return null;
    }

    @Override
    public Void visitArrayAccess(ArrayAccessTree node, Role role) {
        scan(node.getExpression(), Role.EXPRESSION);
        scan(node.getIndex(), Role.INDEX);
        return null;
    }

    @Override
    public Void visitLabeledStatement(LabeledStatementTree node, Role role) {
        return null;
    }

    @Override
    public Void visitLiteral(LiteralTree node, Role role) {
        return null;
    }

    @Override
    public Void visitBindingPattern(BindingPatternTree node, Role role) {
        return null;
    }

    @Override
    public Void visitDefaultCaseLabel(DefaultCaseLabelTree node, Role role) {
        return null;
    }

    @Override
    public Void visitMethod(MethodTree node, Role role) {
        scanIfPresent(node.getModifiers(), Role.MODIFIER);
        scanAll(node.getTypeParameters(), Role.TYPE_PARAMETER);
        scan(node.getReturnType(), Role.TYPE_REFERENCE);
        // name
        scanIfPresent(node.getReceiverParameter(), Role.RECEIVER);
        scanAll(node.getParameters(), Role.VARIABLE);
        scanAll(node.getThrows(), Role.THROWS);
        scanIfPresent(node.getDefaultValue(), Role.DEFAULT_VALUE);
        scanIfPresent(node.getBody(), Role.BLOCK);
        return null;
    }

    @Override
    public Void visitModifiers(ModifiersTree node, Role role) {
        scanAll(node.getAnnotations(), Role.ANNOTATION);
        // flags
        return null;
    }

    @Override
    public Void visitNewArray(NewArrayTree node, Role role) {
        scanAll(node.getAnnotations(), Role.ANNOTATION);
        scan(node.getType(), Role.TYPE_REFERENCE);
        // TODO dim annotations
        scanAll(node.getDimensions(), Role.DIMENSION);
        scanAll(node.getInitializers(), Role.INITIALIZER);
        return null;
    }

    @Override
    public Void visitGuardedPattern(GuardedPatternTree node, Role role) {
        return null;
    }

    @Override
    public Void visitParenthesizedPattern(ParenthesizedPatternTree node, Role role) {
        return null;
    }

    @Override
    public Void visitNewClass(NewClassTree node, Role role) {
        scanIfPresent(node.getEnclosingExpression(), Role.ENCLOSING);
        // new keyword
        scanAll(node.getTypeArguments(), Role.TYPE_ARGUMENT);
        scan(node.getIdentifier(), Role.IDENTIFIER);
        scanAll(node.getArguments(), Role.CONSTRUCTOR_ARGUMENT);
        scanIfPresent(node.getClassBody(), Role.BLOCK);
        return null;
    }

    @Override
    public Void visitLambdaExpression(LambdaExpressionTree node, Role role) {
        scanAll(node.getParameters(), Role.VARIABLE);
        scan(node.getBody());
        return null;
    }

    @Override
    public Void visitPackage(PackageTree node, Role role) {
        // TODO
        return null;
    }

    @Override
    public Void visitParenthesized(ParenthesizedTree node, Role role) {
        node.getExpression().accept(this, role); // skip parentheses for now (?)
        return null;
    }

    @Override
    public Void visitReturn(ReturnTree node, Role role) {
        scanIfPresent(node.getExpression(), Role.EXPRESSION);
        return null;
    }

    @Override
    public Void visitMemberSelect(MemberSelectTree node, Role role) {
        return null;
    }

    @Override
    public Void visitMemberReference(MemberReferenceTree node, Role role) {
        return null;
    }

    @Override
    public Void visitEmptyStatement(EmptyStatementTree node, Role role) {
        return null;
    }

    @Override
    public Void visitSwitch(SwitchTree node, Role role) {
        return null;
    }

    @Override
    public Void visitSwitchExpression(SwitchExpressionTree node, Role role) {
        return null;
    }

    @Override
    public Void visitSynchronized(SynchronizedTree node, Role role) {
        return null;
    }

    @Override
    public Void visitThrow(ThrowTree node, Role role) {
        return null;
    }

    @Override
    public Void visitCompilationUnit(CompilationUnitTree node, Role role) {
        // TODO module
        if (node.getPackage() != null) {
            scanAll(node.getPackageAnnotations(), Role.ANNOTATION);
            scan(node.getPackage(), Role.PACKAGE);
        }
        scanAll(node.getImports(), Role.IMPORT);
        scanAll(node.getTypeDecls(), Role.TYPE);
        return null;
    }

    @Override
    public Void visitTry(TryTree node, Role role) {
        scanAll(node.getResources(), Role.RESOURCE);
        scan(node.getBlock(), Role.BLOCK);
        scanAll(node.getCatches(), Role.CATCH);
        scanIfPresent(node.getFinallyBlock(), Role.FINALLY);
        return null;
    }

    @Override
    public Void visitParameterizedType(ParameterizedTypeTree node, Role role) {
        return null;
    }

    @Override
    public Void visitUnionType(UnionTypeTree node, Role role) {
        return null;
    }

    @Override
    public Void visitIntersectionType(IntersectionTypeTree node, Role role) {
        return null;
    }

    @Override
    public Void visitArrayType(ArrayTypeTree node, Role role) {
        return null;
    }

    @Override
    public Void visitTypeCast(TypeCastTree node, Role role) {
        return null;
    }

    @Override
    public Void visitPrimitiveType(PrimitiveTypeTree node, Role role) {
        return null;
    }

    @Override
    public Void visitTypeParameter(TypeParameterTree node, Role role) {
        return null;
    }

    @Override
    public Void visitInstanceOf(InstanceOfTree node, Role role) {
        return null;
    }

    @Override
    public Void visitUnary(UnaryTree node, Role role) {
        return null;
    }

    @Override
    public Void visitVariable(VariableTree node, Role role) {
        return null;
    }

    @Override
    public Void visitWhileLoop(WhileLoopTree node, Role role) {
        scan(node.getCondition(), Role.CONDITION);
        scan(node.getStatement(), Role.BLOCK);
        return null;
    }

    @Override
    public Void visitWildcard(WildcardTree node, Role role) {
        return null;
    }

    @Override
    public Void visitModule(ModuleTree node, Role role) {
        return null;
    }

    @Override
    public Void visitExports(ExportsTree node, Role role) {
        return null;
    }

    @Override
    public Void visitOpens(OpensTree node, Role role) {
        return null;
    }

    @Override
    public Void visitProvides(ProvidesTree node, Role role) {
        return null;
    }

    @Override
    public Void visitRequires(RequiresTree node, Role role) {
        return null;
    }

    @Override
    public Void visitUses(UsesTree node, Role role) {
        return null;
    }

    @Override
    public Void visitOther(Tree node, Role role) {
        return null;
    }

    @Override
    public Void visitYield(YieldTree node, Role role) {
        scan(node.getValue(), Role.YIELD);
        return null;
    }
}
