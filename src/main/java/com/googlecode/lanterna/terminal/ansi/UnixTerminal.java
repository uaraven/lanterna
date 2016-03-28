/*
 * This file is part of lanterna (http://code.google.com/p/lanterna/).
 *
 * lanterna is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright (C) 2010-2016 Martin
 */
package com.googlecode.lanterna.terminal.ansi;


import java.io.*;
import java.nio.charset.Charset;

import com.googlecode.lanterna.TerminalSize;

/**
 * This class extends UnixLikeTerminal and implements the Unix-specific parts.
 * <p>
 * If you need to have Lanterna to call stty at a different location, you'll need to
 * subclass this and override {@code getSTTYCommand()}.
 *
 * @author Martin
 */
@SuppressWarnings("WeakerAccess")
public class UnixTerminal extends UnixLikeTerminal {

    @Deprecated
    protected final UnixTerminalSizeQuerier terminalSizeQuerier;

    /**
     * Creates a UnixTerminal with default settings, using System.in and System.out for input/output, using the default
     * character set on the system as the encoding and trap ctrl+c signal instead of killing the application.
     * @throws IOException If there was an I/O error initializing the terminal
     */
    public UnixTerminal() throws IOException {
        this(System.in, System.out, Charset.defaultCharset());
    }

    /**
     * Creates a UnixTerminal using a specified input stream, output stream and character set. Ctrl+c signal will be
     * trapped instead of killing the application.
     *
     * @param terminalInput Input stream to read terminal input from
     * @param terminalOutput Output stream to write terminal output to
     * @param terminalCharset Character set to use when converting characters to bytes
     * @throws java.io.IOException If there was an I/O error initializing the terminal
     */
    public UnixTerminal(
            InputStream terminalInput,
            OutputStream terminalOutput,
            Charset terminalCharset) throws IOException {
        this(terminalInput, terminalOutput, terminalCharset, CtrlCBehaviour.CTRL_C_KILLS_APPLICATION);
    }

    /**
     * Creates a UnixTerminal using a specified input stream, output stream and character set, with a custom size
     * querier instead of using the default one. This way you can override size detection (if you want to force the
     * terminal to a fixed size, for example). Ctrl+c signal will be trapped instead of killing the application.
     *
     * @param terminalInput Input stream to read terminal input from
     * @param terminalOutput Output stream to write terminal output to
     * @param terminalCharset Character set to use when converting characters to bytes
     * @param customSizeQuerier Object to use for looking up the size of the terminal, or null to use the built-in
     * method
     * @throws java.io.IOException If there was an I/O error initializing the terminal
     * @deprecated Use an overload that doesn't take a {@link UnixTerminalSizeQuerier}
     */
    @SuppressWarnings({"SameParameterValue", "WeakerAccess"})
    @Deprecated
    public UnixTerminal(
            InputStream terminalInput,
            OutputStream terminalOutput,
            Charset terminalCharset,
            UnixTerminalSizeQuerier customSizeQuerier) throws IOException {
        this(terminalInput, terminalOutput, terminalCharset, customSizeQuerier, CtrlCBehaviour.CTRL_C_KILLS_APPLICATION);
    }

    /**
     * Creates a UnixTerminal using a specified input stream, output stream and character set, with a custom size
     * querier instead of using the default one. This way you can override size detection (if you want to force the
     * terminal to a fixed size, for example). You also choose how you want ctrl+c key strokes to be handled.
     *
     * @param terminalInput Input stream to read terminal input from
     * @param terminalOutput Output stream to write terminal output to
     * @param terminalCharset Character set to use when converting characters to bytes
     * @param terminalCtrlCBehaviour Special settings on how the terminal will behave, see {@code UnixTerminalMode} for more
     * details
     * @throws java.io.IOException If there was an I/O error initializing the terminal
     */
    @SuppressWarnings({"SameParameterValue", "WeakerAccess"})
    public UnixTerminal(
            InputStream terminalInput,
            OutputStream terminalOutput,
            Charset terminalCharset,
            CtrlCBehaviour terminalCtrlCBehaviour) throws IOException {

        this(terminalInput, terminalOutput, terminalCharset, null, terminalCtrlCBehaviour);
    }

    /**
     * Creates a UnixTerminal using a specified input stream, output stream and character set, with a custom size
     * querier instead of using the default one. This way you can override size detection (if you want to force the
     * terminal to a fixed size, for example). You also choose how you want ctrl+c key strokes to be handled.
     *
     * @param terminalInput Input stream to read terminal input from
     * @param terminalOutput Output stream to write terminal output to
     * @param terminalCharset Character set to use when converting characters to bytes
     * @param customSizeQuerier Object to use for looking up the size of the terminal, or null to use the built-in
     * method
     * @param terminalCtrlCBehaviour Special settings on how the terminal will behave, see {@code UnixTerminalMode} for more
     * details
     * @throws java.io.IOException If there was an I/O error initializing the terminal
     * @deprecated Use an overload that doesn't take a {@link UnixTerminalSizeQuerier}
     */
    @SuppressWarnings({"SameParameterValue", "WeakerAccess"})
    @Deprecated
    public UnixTerminal(
            InputStream terminalInput,
            OutputStream terminalOutput,
            Charset terminalCharset,
            UnixTerminalSizeQuerier customSizeQuerier,
            CtrlCBehaviour terminalCtrlCBehaviour) throws IOException {

        this(new STTYTerminalDeviceController(new File("/dev/tty")),
                terminalInput,
                terminalOutput,
                terminalCharset,
                customSizeQuerier,
                terminalCtrlCBehaviour);
    }

    /**
     * Creates a UnixTerminal using a specified input stream, output stream and character set, with a custom size
     * querier instead of using the default one. This way you can override size detection (if you want to force the
     * terminal to a fixed size, for example). You also choose how you want ctrl+c key strokes to be handled.
     *
     * @param deviceController Terminal device controller to use instead of the default one
     * @param terminalInput Input stream to read terminal input from
     * @param terminalOutput Output stream to write terminal output to
     * @param terminalCharset Character set to use when converting characters to bytes
     * @param terminalCtrlCBehaviour Special settings on how the terminal will behave, see {@code UnixTerminalMode} for more
     * details
     * @throws java.io.IOException If there was an I/O error initializing the terminal
     */
    public UnixTerminal(
            TerminalDeviceControlStrategy deviceController,
            InputStream terminalInput,
            OutputStream terminalOutput,
            Charset terminalCharset,
            CtrlCBehaviour terminalCtrlCBehaviour) throws IOException {

        this(deviceController,
                terminalInput,
                terminalOutput,
                terminalCharset,
                null,
                terminalCtrlCBehaviour);
    }

    private UnixTerminal(
            TerminalDeviceControlStrategy deviceController,
            InputStream terminalInput,
            OutputStream terminalOutput,
            Charset terminalCharset,
            UnixTerminalSizeQuerier customSizeQuerier,
            CtrlCBehaviour terminalCtrlCBehaviour) throws IOException {

        super(deviceController,
                terminalInput,
                terminalOutput,
                terminalCharset,
                terminalCtrlCBehaviour);

        this.terminalSizeQuerier = customSizeQuerier;
    }

    @Override
    public TerminalSize getTerminalSize() throws IOException {
        if(terminalSizeQuerier != null) {
            return terminalSizeQuerier.queryTerminalSize();
        }
        
        return super.getTerminalSize();
    }
}
