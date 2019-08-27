package eu.xenit.custodian.sentinel.reporting.io;

import java.io.Writer;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public class JsonWriter extends IndentingWriter {

    public JsonWriter(Writer out) {
        super(out);
    }

    public JsonWriter(Writer out, Function<Integer, String> indentStrategy) {
        super(out, indentStrategy);
    }

    public void writeProperty(String name, Runnable value) {
        this.print("\"" + name + "\": ");
        value.run();
    }

    public void writeArray(Runnable... content) {
        if (content.length == 0)
        {
            this.print("[]");
            return;
        }

        this.println("[");
        this.indented(() -> {
            this.joinRunnables(Stream.of(content), () -> this.println(","), this::println);
        });
        this.print("]");
    }

    @SafeVarargs
    public final void writeObject(Optional<Runnable>... fields) {
        this.writeObject(Arrays.stream(fields).filter(Optional::isPresent).map(Optional::get));
    }

    public void writeObject(Runnable... fields) {
        this.writeObject(Stream.of(fields));
    }

    public void writeObject(Stream<Runnable> fields) {
        this.println("{");
        this.indented(() -> {
            this.joinRunnables(fields, () -> this.println(","), this::println);
        });
        this.print("}");
    }

    private void joinRunnables (Stream<Runnable> content, Runnable joiner, Runnable end) {
        Iterator<Runnable> iterator = content.iterator();
        while (iterator.hasNext()) {
            iterator.next().run();

            if (iterator.hasNext()) {
                joiner.run();
            } else {
                end.run();
            }
        }
    }
}
