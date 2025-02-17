package org.itsallcode.openfasttrace.importer.specobject.xml.tree;

import java.util.ArrayDeque;
import java.util.Deque;

import org.itsallcode.openfasttrace.importer.specobject.xml.ContentHandlerAdapterController;
import org.itsallcode.openfasttrace.importer.specobject.xml.EventContentHandler;
import org.itsallcode.openfasttrace.importer.specobject.xml.event.EndElementEvent;
import org.itsallcode.openfasttrace.importer.specobject.xml.event.StartElementEvent;

/**
 * A {@link EventContentHandler} that builds an XML element tree.
 */
public class TreeBuildingContentHandler implements EventContentHandler, TreeParsingController
{
    private final Deque<TreeElement> stack = new ArrayDeque<>();
    private TreeContentHandler delegate;
    private ContentHandlerAdapterController contentHandlerAdapter;

    /**
     * Create a new instance.
     * 
     * @param delegate
     *            the delegate.
     */
    public TreeBuildingContentHandler(final TreeContentHandler delegate)
    {
        this.delegate = delegate;
    }

    @Override
    public void startElement(final StartElementEvent event)
    {
        final TreeElement treeElement = new TreeElement(event, this.stack.peek());
        this.stack.push(treeElement);
        this.getDelegate().startElement(treeElement);
    }

    @Override
    public void endElement(final EndElementEvent event)
    {
        if (this.stack.isEmpty())
        {
            throw new IllegalStateException("Got closing event " + event + " but stack is empty");
        }
        final TreeElement topElement = this.stack.peek();
        if (!topElement.getElement().getName().equals(event.getName()))
        {
            throw new IllegalStateException("Top stack element is " + topElement.getElement()
                    + " but got end event for " + event);
        }
        final TreeElement closedElement = this.stack.pop();
        this.getDelegate().endElement(closedElement);
    }

    @Override
    public void characters(final String characters)
    {
        if (this.stack.isEmpty())
        {
            throw new IllegalStateException("Got characters " + characters + " but stack is empty");
        }
        this.stack.peek().addCharacterData(characters);
    }

    @Override
    public void init(final ContentHandlerAdapterController contentHandlerAdapter)
    {
        this.contentHandlerAdapter = contentHandlerAdapter;
        this.getDelegate().init(this);
    }

    private TreeContentHandler getDelegate()
    {
        if (this.delegate == null)
        {
            throw new IllegalStateException("No delegate");
        }
        return this.delegate;
    }

    @Override
    public void setDelegate(final TreeContentHandler newDelegate)
    {
        this.delegate = newDelegate;
        newDelegate.init(this);
    }

    @Override
    public TreeElement getCurrentElement()
    {
        if (this.stack.isEmpty())
        {
            throw new IllegalStateException("Stack is empty");
        }
        return this.stack.peek();
    }

    @Override
    public void stopParsing()
    {
        this.contentHandlerAdapter.parsingFinished();
    }
}
