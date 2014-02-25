
/**
 * Created by chris on 24/02/2014.
 */
public class Generate extends AbstractGenerate {

    @Override
    public void reportError(Token token, String explanatoryMessage) throws CompilationException {
        throw new CompilationException(explanatoryMessage);
    }
}
