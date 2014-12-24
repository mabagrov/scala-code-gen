package net.yefremov.sample.codegen.ast

import net.yefremov.sample.codegen.Generator
import net.yefremov.sample.codegen.schema.{FieldType, TypeSchema}
import net.yefremov.sample.codegen.schema.FieldType.FieldType

import treehugger.forest._
import treehuggerDSL._
import definitions._

/**
 * An AST generator based on the 'treehugger' livrary.
 * @author Dmitriy Yefremov
 */
class TreehuggerGenerator extends Generator {

  override def generate(schema: TypeSchema): String = {
    val classSymbol = RootClass.newClass(schema.name.shortName)
    val params = schema.fields.map(field => PARAM(field.name, toType(field.valueType)): ValDef)
    val tree = BLOCK(
      CASECLASSDEF(classSymbol).withParams(params).tree.withDoc(schema.comment):= BLOCK(
        DEF("schema", StringClass) := LIT(schema.toString)
      )
      ).inPackage(schema.name.packageName)

    treeToString(tree)
  }

  private def toType(fieldType: FieldType): Type = {
    fieldType match {
      case FieldType.String => StringClass
      case FieldType.Integer => IntClass
      case FieldType.Boolean => BooleanClass
    }
  }

}
