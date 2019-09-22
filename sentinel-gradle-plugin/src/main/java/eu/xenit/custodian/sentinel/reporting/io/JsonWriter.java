package eu.xenit.custodian.sentinel.reporting.io;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JsonWriter {

    private final IndentingWriter writer;

    public JsonWriter(IndentingWriter out) {
        if (out == null) {
            throw new IllegalArgumentException("out must not be null");
        }

        this.writer = out;
    }

    public Runnable value(boolean bool) {
        return this.value(String.valueOf(bool));
    }

    public Runnable value(String value) {
        return () -> {
            if (value != null) {
                this.writer.print("\"" + value + "\"");
            } else {
                // https://tools.ietf.org/html/rfc8259#section-3
                this.writer.print("null");
            }
        };
    }

    public Runnable value(Object object) {
        String value = object != null ? object.toString() : "";
        return () -> this.writer.print("\"" + value + "\"");
    }

    public Runnable property(String name, String value) {
        return property(name, value(value));
    }

    public Runnable property(String name, Runnable value) {
        return () -> {
            this.writer.print("\"" + name + "\": ");
            value.run();
        };
    }

    public Optional<Runnable> property(String name, Optional<Runnable> value) {
        Objects.requireNonNull(value, "Optional<Runnable> value must not be null");

        return value.map(runnable -> () -> {
            this.writer.print("\"" + name + "\": ");
            runnable.run();
        });

    }

    public Runnable array(Runnable... content) {
        return array(Arrays.asList(content));
    }

    public <T> Runnable array(Collection<T> collection, Function<T, Runnable> callback) {
        return this.array(collection.stream(), callback);
    }

    public <T> Runnable array(Stream<T> collection, Function<T, Runnable> callback) {
        return this.array(collection
                .map(callback::apply)
                .collect(Collectors.toList()));

    }

    public Runnable array(Collection<Runnable> content) {
        return () -> {
            if (content.size() == 0) {
                this.writer.print("[]");
                return;
            }

            this.writer.println("[");
            this.writer.indented(() -> {
                this.join(content, () -> this.writer.println(","), this.writer::println);
            });
            this.writer.print("]");
        };
    }

    public Runnable object() {
        return () -> this.writer.print("{}");
    }

    @SafeVarargs
    public final Runnable object(Optional<Runnable>... fields) {
        return this.object(Stream.of(fields).filter(Optional::isPresent).map(Optional::get));
    }

    public Runnable object(Runnable... fields) {
        return this.object(Stream.of(fields));
    }

    public Runnable object(Stream<Runnable> fields) {
        return object(fields.collect(Collectors.toList()));
    }

    public Runnable object(Collection<Runnable> fields) {
        if (fields.size() == 0) {
            return this.object();
        }

        return () -> {
            this.writer.println("{");
            this.writer.indented(() -> {
                this.join(fields, () -> this.writer.println(","), this.writer::println);
            });
            this.writer.print("}");
        };
    }

    private void join(Collection<Runnable> content, Runnable joiner, Runnable end) {
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
