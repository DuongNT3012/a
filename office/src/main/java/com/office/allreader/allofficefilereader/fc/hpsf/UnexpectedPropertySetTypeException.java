

package com.office.allreader.allofficefilereader.fc.hpsf;

/**
 * <p>This exception is thrown if a certain type of property set is
 * expected (e.g. a Document Summary Information) but the provided
 * property set is not of that type.</p>
 *
 * <p>The constructors of this class are analogous to those of its
 * superclass and documented there.</p>
 *
 * @author Rainer Klute <a
 * href="mailto:klute@rainer-klute.de">&lt;klute@rainer-klute.de&gt;</a>
 */
public class UnexpectedPropertySetTypeException extends HPSFException
{

    /**
     * <p>Creates an {@link UnexpectedPropertySetTypeException}.</p>
     */
    public UnexpectedPropertySetTypeException()
    {
        super();
    }


    /**
     * <p>Creates an {@link UnexpectedPropertySetTypeException} with a message
     * string.</p>
     *
     * @param msg The message string.
     */
    public UnexpectedPropertySetTypeException(final String msg)
    {
        super(msg);
    }


    /**
     * <p>Creates a new {@link UnexpectedPropertySetTypeException} with a
     * reason.</p>
     *
     * @param reason The reason, i.e. a throwable that indirectly
     * caused this exception.
     */
    public UnexpectedPropertySetTypeException(final Throwable reason)
    {
        super(reason);
    }


    /**
     * <p>Creates an {@link UnexpectedPropertySetTypeException} with a message
     * string and a reason.</p>
     *
     * @param msg The message string.
     * @param reason The reason, i.e. a throwable that indirectly
     * caused this exception.
     */
    public UnexpectedPropertySetTypeException(final String msg,
                                              final Throwable reason)
    {
        super(msg, reason);
    }

}