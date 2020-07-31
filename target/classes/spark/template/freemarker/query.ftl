<#assign content>

<h1> <br> ${title} <br></h1>

<p>


<textarea id="companyname" name="companyname" rows="1" cols="15">2</textarea>
<textarea id="currPrice" name="currPrice" rows="1" cols="15">2</textarea>
<textarea id="interest" name="interest" rows="1" cols="15">2</textarea>
<textarea id="volatility" name="volatility" rows="1" cols="15">2</textarea>
<textarea id="time" name="time" rows="1" cols="15">2</textarea>




<div class="input_box">
  <div class="header">
    Price Expectations at Expiry Date
  </div>
  <div class="row">
    <div class="blocc"></div>
    <div class="low"></div>
    <div class="blocc"></div>
    <div class="mid"></div>
    <div class="blocc"></div>
    <div class="high"></div>
    <div class="blocc"></div>
  </div>

  <div class="submit_row">
    <div class="lownum">
      Downside Expectation:<br>
      <select name="downsideProb" id="downsideProb" class="querybox">
        <option value="zero">0 (N/A)</option>
        <option value="one">1 (Most Unlikely)</option>
        <option value="two">2</option>
        <option value="three">3 (Neutral)</option>
        <option value="four">4</option>
        <option value="five">5(Most Likely)</option>
      </select><br>
      <input class="queryinput" style='width:76%;' placeHolder="Range Lower Limit" type="number" step="0.01" name="downsideLower" id="downsideLower"><br>
      <input class="queryinput" style='width:76%;' placeHolder="Range Upper Limit" type="number" step="0.01" name="downsideUpper" id="downsideUpper">
    </div>
    <div class="midnum">
      Mid-range Expectation:<br>
      <select name="midrangeProb" id="midrangeProb" class="querybox">
        <option value="one">1 (Most Unlikely)</option>
        <option value="two">2</option>
        <option value="three" selected>3 (Neutral)</option>
        <option value="four">4</option>
        <option value="five">5(Most Likely)</option>
      </select><br>
      <input class="queryinput" style='width:76%;' placeHolder="0.00" type="number" step="0.01" name="midrangeLower" id="midrangeLower"><br>
      <input class="queryinput" style='width:76%;' placeHolder="1000.00" type="number" step="0.01" name="midrangeUpper" id="midrangeUpper">

    </div>
    <div class="highnum">
      Upside Expectation:<br>
      <select name="upsideProb" id="upsideProb" class="querybox">
        <option value="zero">0 (N/A)</option>
        <option value="one">1 (Most Unlikely)</option>
        <option value="two">2</option>
        <option value="three">3 (Neutral)</option>
        <option value="four">4</option>
        <option value="five">5(Most Likely)</option>
      </select><br>
      <input class="queryinput" style='width:76%;' placeHolder="Range Lower Limit" type="number" step="0.01" name="upsideLower" id="upsideLower"><br>
      <input class="queryinput" style='width:76%;' placeHolder="Range Upper Limit" type="number" step="0.01" name="upsideUpper" id="upsideUpper">

    </div>
  </div>

</div>

<br>
<div class="submit_row">
  <button type="button" name="submit" id="submit">Submit</button>
</div>

<div name="error" id="error">

</div>


</p>

<div class="input_box" style="overflow-x:auto;">

  <table name="portfolio" id="portfolio">

  </table>

  <div name="buttons" id="buttons" class="constain">
    <button name="add_button" id="add_button" class="add_button">+</button>
    <button name="sub_button" id="sub_button" class="sub_button">-</button>
    <br>
    <button type="button" name="refresh" id="refresh">Refresh</button>
    <div name="error2" id="error2">
  </div>

<br>
<br>


  <div id="chartContainer" style="height: 300px; width: 100%;"></div>
</div>

</#assign>
<#include "main.ftl">
