"use client";

import { motion } from "framer-motion";
import {
  Accordion,
  AccordionContent,
  AccordionItem,
  AccordionTrigger,
} from "@/components/ui/accordion";

const faqs = [
  {
    question: "How does CareerPilot AI find jobs?",
    answer:
      "Our multi-agent AI system searches across multiple job platforms including LinkedIn, Indeed, and more. It analyzes job descriptions against your profile to find the best matches based on skills, experience, and preferences.",
  },
  {
    question: "Is my data safe?",
    answer:
      "Absolutely. We use industry-standard encryption for all data. Your resume and personal information are never shared with third parties. You control your data at all times.",
  },
  {
    question: "How does the auto-apply feature work?",
    answer:
      "CareerPilot AI tailors your resume and cover letter for each position, then submits applications on your behalf. Each application is personalized — no generic mass applications.",
  },
  {
    question: "Can I customize which jobs to apply for?",
    answer:
      "Yes. You set filters for job title, location, salary range, company size, and more. You can also review applications before they're submitted or let AI handle everything automatically.",
  },
  {
    question: "What makes this different from other job boards?",
    answer:
      "CareerPilot AI doesn't just list jobs — it actively works for you. It optimizes your resume per application, tracks responses, prepares you for interviews, and learns from outcomes to improve over time.",
  },
  {
    question: "Is there a free plan?",
    answer:
      "Yes. We offer a free tier with limited job searches and applications per month. Premium plans unlock unlimited applications, priority matching, and advanced AI features.",
  },
];

export function FaqSection() {
  return (
    <section id="faq" className="py-24">
      <div className="mx-auto max-w-3xl px-4 sm:px-6 lg:px-8">
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          whileInView={{ opacity: 1, y: 0 }}
          viewport={{ once: true }}
          className="text-center mb-12"
        >
          <h2 className="text-3xl sm:text-4xl font-bold">
            Frequently Asked{" "}
            <span className="bg-gradient-to-r from-primary to-accent bg-clip-text text-transparent">
              Questions
            </span>
          </h2>
        </motion.div>

        <motion.div
          initial={{ opacity: 0, y: 20 }}
          whileInView={{ opacity: 1, y: 0 }}
          viewport={{ once: true }}
        >
          <Accordion type="single" collapsible className="w-full">
            {faqs.map((faq, index) => (
              <AccordionItem key={index} value={`item-${index}`}>
                <AccordionTrigger className="text-left">
                  {faq.question}
                </AccordionTrigger>
                <AccordionContent className="text-muted-foreground">
                  {faq.answer}
                </AccordionContent>
              </AccordionItem>
            ))}
          </Accordion>
        </motion.div>
      </div>
    </section>
  );
}
