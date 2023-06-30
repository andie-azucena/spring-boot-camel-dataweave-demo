package com.example;

import java.io.File;
import java.util.HashMap;

import org.apache.camel.Exchange;
import org.mule.weave.v2.runtime.DataWeaveResult;
import org.mule.weave.v2.runtime.DataWeaveScript;
import org.mule.weave.v2.runtime.DataWeaveScriptingEngine;
import org.mule.weave.v2.runtime.ScriptingBindings;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/****
 * DataTransformer
 * Description: Contains sample transform*() methods.
 *
 * transformFromFile(File, Exchange) -> This accepts a .dwl file and the Camel
 * Exchange message.
 * transform(String) and transformWithVariables(String) -> Sample methods to
 * test embedded DW script.
 *
 * @author Andie Azucena
 */
public class DataTransformer {

  public Object transformFromFile(File file, Exchange ex) throws Exception {
    DataWeaveScriptingEngine dwScriptingEngine = new DataWeaveScriptingEngine();
    DataWeaveScript compile = dwScriptingEngine.compile(file, new String[] { "payload" });
    DataWeaveResult result = compile.write(new ScriptingBindings()
        .addBinding("payload", ex.getIn().getBody(), "application/java", new HashMap<>()));

    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.readValue(result.getContentAsString(), new TypeReference<Object>() {
    });
  }

  public String transform(String payload) throws Exception {
    DataWeaveScriptingEngine dwScriptingEngine = new DataWeaveScriptingEngine();
    String script = "%dw 2.0\n" +
        "output application/json\n" +
        "---\n" +
        "payload";

    DataWeaveScript compile = dwScriptingEngine.compile(script, new String[] { "payload", "vars" });
    DataWeaveResult result = compile.write(new ScriptingBindings()
        .addBinding("payload", payload, "application/java", new HashMap<>()));

    return result.getContentAsString();
  }

  public String transformWithVariable(String payload) throws Exception {
    DataWeaveScriptingEngine dwScriptingEngine = new DataWeaveScriptingEngine();
    String script = "%dw 2.0\n" +
        "output application/json\n" +
        "---\n" +
        "{\"message\": upper(payload), \"vars\": vars }";

    DataWeaveScript compile = dwScriptingEngine.compile(script, new String[] { "payload", "vars" });
    DataWeaveResult result = compile.write(new ScriptingBindings()
        .addBinding("payload", payload, "application/java", new HashMap<>())
        .addBinding("vars", "my_variable", "application/java", new HashMap<>()));

    return result.getContentAsString();
  }
}
