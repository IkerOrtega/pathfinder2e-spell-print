package com.ikerortega.spell_printer.spell;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.ikerortega.spell_printer.util.CSVReaderUtil;
import com.ikerortega.spell_printer.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/spell")
public class SpellRetriever {

    private final ObjectWriter ow = new ObjectMapper().setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY).writer().withDefaultPrettyPrinter();
    private final Logger logger = LoggerFactory.getLogger(SpellRetriever.class);

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("")
    public String getSpellByTitle(@RequestParam(required = false, defaultValue = "") String title) {
        String output = "";
        try {
            List<Spell> spellList = CSVReaderUtil.readCSV(Constants.SPELLS_CSV_PATH, Spell.class, title);
            SpellResponse spellResponse = new SpellResponse();
            spellResponse.filter = title;
            spellResponse.spellList = spellList;
            spellResponse.size = spellList.size();
            output = ow.writeValueAsString(spellResponse);

        } catch (Exception e) {
            logger.error("Error retrieving spells: ", e);
        }
        return title + "\n" + output;
    }
}
