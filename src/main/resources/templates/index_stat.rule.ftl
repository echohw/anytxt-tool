<?xml version="1.0" encoding="UTF-8" standalone="yes" ?>
<!DOCTYPE boost_serialization>
<boost_serialization signature="serialization::archive" version="13">
  <dataObj class_id="0" tracking_level="0" version="0">
    <m_ruleType>${ruleType!0}</m_ruleType>
    <m_qslPaths class_id="1" tracking_level="0" version="0">
      <stdList class_id="2" tracking_level="0" version="0">
        <count>${count?c!0}</count>
        <item_version>${version!0}</item_version>
        <#if dirList??>
          <#list dirList as dir>
            <#if dir_index == 0>
              <item class_id="3" tracking_level="0" version="0">
                <stdString>${dir}</stdString>
              </item>
            <#else>
              <item>
                <stdString>${dir}</stdString>
              </item>
            </#if>
          </#list>
        </#if>
      </stdList>
    </m_qslPaths>
  </dataObj>
